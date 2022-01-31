package io.openfuture.api.service

import io.openfuture.api.component.scaffold.processor.ScaffoldProcessor
import io.openfuture.api.component.state.StateApi
import io.openfuture.api.config.propety.ScaffoldProperties
import io.openfuture.api.domain.holder.AddEthereumShareHolderRequest
import io.openfuture.api.domain.holder.UpdateEthereumShareHolderRequest
import io.openfuture.api.domain.scaffold.*
import io.openfuture.api.entity.auth.User
import io.openfuture.api.entity.scaffold.EthereumScaffold
import io.openfuture.api.entity.scaffold.EthereumScaffoldProperty
import io.openfuture.api.entity.scaffold.EthereumScaffoldSummary
import io.openfuture.api.entity.state.Blockchain
import io.openfuture.api.exception.NotFoundException
import io.openfuture.api.repository.EthereumScaffoldPropertyRepository
import io.openfuture.api.repository.EthereumScaffoldRepository
import io.openfuture.api.repository.EthereumScaffoldSummaryRepository
import io.openfuture.api.repository.ShareHolderRepository
import org.apache.commons.lang3.time.DateUtils.addMinutes
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class DefaultEthereumScaffoldService(
        private val processor: ScaffoldProcessor,
        private val properties: ScaffoldProperties,
        private val ethereumScaffoldRepository: EthereumScaffoldRepository,
        private val propertyRepository: EthereumScaffoldPropertyRepository,
        private val ethereumScaffoldSummaryRepository: EthereumScaffoldSummaryRepository,
        private val shareHolderRepository: ShareHolderRepository,
        private val stateApi: StateApi
) : EthereumScaffoldService {

    @Transactional(readOnly = true)
    override fun getAll(user: User, pageRequest: Pageable): Page<EthereumScaffold> =
            ethereumScaffoldRepository.findAll(pageRequest)

    @Transactional(readOnly = true)
    override fun get(address: String, user: User): EthereumScaffold = ethereumScaffoldRepository.findByAddress(address)
            ?: throw NotFoundException("Not found scaffold with address $address")

    @Transactional(readOnly = true)
    override fun get(address: String): EthereumScaffold = ethereumScaffoldRepository.findByAddress(address)
            ?: throw NotFoundException("Not found scaffold with address $address")

    @Transactional(readOnly = true)
    override fun compile(request: CompileEthereumScaffoldRequest, user: User): CompiledScaffoldDto {
        if (ethereumScaffoldSummaryRepository.countByEnabledIsFalse() >= properties.allowedDisabledContracts) {
            throw IllegalStateException("Disabled scaffold count is more than allowed")
        }

        return processor.compile(request)
    }

    @Transactional
    override fun deploy(request: DeployEthereumScaffoldRequest, user: User): EthereumScaffold {
        val compiledScaffold = compile(CompileEthereumScaffoldRequest(request.properties, request.version), user)
        val contractAddress = processor.deploy(compiledScaffold.bin, request)
        return save(
            SaveEthereumScaffoldRequest(
                    contractAddress,
                    compiledScaffold.abi,
                    request.developerAddress,
                    request.description,
                    request.fiatAmount,
                    request.currency,
                    request.conversionAmount,
                    request.webHook,
                    request.properties,
                    request.version
            ), user
        )
    }

    @Transactional
    override fun save(request: SaveEthereumScaffoldRequest, user: User): EthereumScaffold {
        val scaffold = ethereumScaffoldRepository.save(EthereumScaffold.of(request))
        val properties = request.properties.map { propertyRepository.save(EthereumScaffoldProperty.of(scaffold, it)) }
        scaffold.property.addAll(properties)
        getScaffoldSummary(scaffold.address, user, true)
        request.webHook?.let { stateApi.createWallet(scaffold.developerAddress, it, Blockchain.Ethereum) }
        return scaffold
    }

    @Transactional
    override fun update(address: String, user: User, request: UpdateEthereumScaffoldRequest): EthereumScaffold {
        val scaffold = get(address, user)

        scaffold.description = request.description!!

        return ethereumScaffoldRepository.save(scaffold)
    }

    @Transactional
    override fun setWebHook(address: String, request: SetWebHookRequest, user: User): EthereumScaffold {
        val scaffold = get(address, user)

        scaffold.webHook = request.webHook

        return ethereumScaffoldRepository.save(scaffold)
    }

    @Transactional(readOnly = true)
    override fun getQuota(user: User): EthereumScaffoldQuotaDto {
        val scaffoldCount = ethereumScaffoldSummaryRepository.countByEnabledIsFalse()
        return EthereumScaffoldQuotaDto(scaffoldCount, properties.allowedDisabledContracts)
    }

    @Transactional
    override fun getScaffoldSummary(address: String, user: User, force: Boolean): EthereumScaffoldSummary {
        val scaffold = get(address, user)
        val cacheSummary = ethereumScaffoldSummaryRepository.findByEthereumScaffold(scaffold)
        if (!force && null != cacheSummary && addMinutes(cacheSummary.date, properties.cachePeriodInMinutest).after(Date())) {
            return cacheSummary
        }

        val summary = processor.getScaffoldSummary(scaffold)
        cacheSummary?.let { summary.id = it.id }
        val persistSummary = ethereumScaffoldSummaryRepository.save(summary)
        shareHolderRepository.deleteAllBySummary(summary)
        val shareHolders = processor.getShareHolders(persistSummary).map { shareHolderRepository.save(it) }
        persistSummary.ethereumShareHolders.addAll(shareHolders)
        return persistSummary
    }

    @Transactional
    override fun deactivate(address: String, user: User): EthereumScaffoldSummary {
        val scaffold = get(address, user)
        processor.deactivate(scaffold)
        return getScaffoldSummary(address, user, true)
    }

    @Transactional
    override fun activate(address: String, user: User): EthereumScaffoldSummary {
        val scaffold = get(address, user)
        processor.activate(scaffold)
        return getScaffoldSummary(address, user, true)
    }

    @Transactional
    override fun addShareHolder(address: String, user: User, request: AddEthereumShareHolderRequest): EthereumScaffoldSummary {
        val scaffold = get(address, user)
        processor.addShareHolder(scaffold, request.address!!, request.percent!!.toLong())
        return getScaffoldSummary(address, user, true)
    }

    @Transactional
    override fun updateShareHolder(address: String, user: User,
                                   holderAddress: String, request: UpdateEthereumShareHolderRequest): EthereumScaffoldSummary {
        val scaffold = get(address, user)
        processor.updateShareHolder(scaffold, holderAddress, request.percent!!.toLong())
        return getScaffoldSummary(address, user, true)
    }

    @Transactional
    override fun removeShareHolder(address: String, user: User, holderAddress: String): EthereumScaffoldSummary {
        val scaffold = get(address, user)
        processor.removeShareHolder(scaffold, holderAddress)
        return getScaffoldSummary(address, user, true)
    }

}
