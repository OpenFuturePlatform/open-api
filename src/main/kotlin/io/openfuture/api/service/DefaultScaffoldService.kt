package io.openfuture.api.service

import io.openfuture.api.component.scaffold.processor.ScaffoldProcessor
import io.openfuture.api.component.state.StateApi
import io.openfuture.api.config.propety.ScaffoldProperties
import io.openfuture.api.domain.holder.AddShareHolderRequest
import io.openfuture.api.domain.holder.UpdateShareHolderRequest
import io.openfuture.api.domain.scaffold.*
import io.openfuture.api.entity.auth.OpenKey
import io.openfuture.api.entity.auth.User
import io.openfuture.api.entity.scaffold.Blockchain.Ethereum
import io.openfuture.api.entity.scaffold.Scaffold
import io.openfuture.api.entity.scaffold.ScaffoldProperty
import io.openfuture.api.entity.scaffold.ScaffoldSummary
import io.openfuture.api.exception.NotFoundException
import io.openfuture.api.repository.ScaffoldPropertyRepository
import io.openfuture.api.repository.ScaffoldRepository
import io.openfuture.api.repository.ScaffoldSummaryRepository
import io.openfuture.api.repository.ShareHolderRepository
import org.apache.commons.lang3.time.DateUtils.addMinutes
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class DefaultScaffoldService(
        private val processor: ScaffoldProcessor,
        private val properties: ScaffoldProperties,
        private val repository: ScaffoldRepository,
        private val propertyRepository: ScaffoldPropertyRepository,
        private val summaryRepository: ScaffoldSummaryRepository,
        private val shareHolderRepository: ShareHolderRepository,
        private val openKeyService: OpenKeyService,
        private val stateApi: StateApi
) : ScaffoldService {

    @Transactional(readOnly = true)
    override fun getAll(user: User, pageRequest: Pageable): Page<Scaffold> =
            repository.findAllByOpenKeyUserOrderByIdDesc(user, pageRequest)

    @Transactional(readOnly = true)
    override fun get(address: String, user: User): Scaffold = repository.findByAddressAndOpenKeyUser(address, user)
            ?: throw NotFoundException("Not found scaffold with address $address")

    @Transactional(readOnly = true)
    override fun get(address: String): Scaffold = repository.findByAddress(address)
            ?: throw NotFoundException("Not found scaffold with address $address")

    @Transactional(readOnly = true)
    override fun compile(request: CompileScaffoldRequest): CompiledScaffoldDto {
        val openKey = openKeyService.get(request.openKey!!)
        if (summaryRepository.countByEnabledIsFalseAndScaffoldOpenKeyUser(openKey.user) >= properties.allowedDisabledContracts) {
            throw IllegalStateException("Disabled scaffold count is more than allowed")
        }

        return processor.compile(request)
    }

    @Transactional
    override fun deploy(request: DeployScaffoldRequest): Scaffold {
        val compiledScaffold = compile(CompileScaffoldRequest(request.openKey, request.properties, request.version))
        val contractAddress = processor.deploy(compiledScaffold.bin, request)
        return save(SaveScaffoldRequest(
                contractAddress,
                compiledScaffold.abi,
                request.openKey,
                request.developerAddress,
                request.description,
                request.fiatAmount,
                request.currency,
                request.conversionAmount,
                request.webHook,
                request.properties,
                request.version
        ))
    }

    @Transactional
    override fun save(request: SaveScaffoldRequest): Scaffold {
        val openKey = openKeyService.get(request.openKey!!)
        val scaffold = repository.save(Scaffold.of(request, openKey))
        val properties = request.properties.map { propertyRepository.save(ScaffoldProperty.of(scaffold, it)) }
        scaffold.property.addAll(properties)
        getScaffoldSummary(scaffold.address, openKey.user, true)
        return scaffold
    }

    @Transactional
    override fun update(address: String, user: User, request: UpdateScaffoldRequest): Scaffold {
        val scaffold = get(address, user)

        scaffold.description = request.description!!

        return repository.save(scaffold)
    }

    @Transactional
    override fun setWebHook(address: String, request: SetWebHookRequest, user: User): Scaffold {
        val scaffold = get(address, user)

        scaffold.webHook = request.webHook

        updateStateWebHook(scaffold.openKey, request.webHook)

        return repository.save(scaffold)
    }

    @Transactional(readOnly = true)
    override fun getQuota(user: User): ScaffoldQuotaDto {
        val scaffoldCount = summaryRepository.countByEnabledIsFalseAndScaffoldOpenKeyUser(user)
        return ScaffoldQuotaDto(scaffoldCount, properties.allowedDisabledContracts)
    }

    @Transactional
    override fun getScaffoldSummary(address: String, user: User, force: Boolean): ScaffoldSummary {
        val scaffold = get(address, user)
        val cacheSummary = summaryRepository.findByScaffold(scaffold)
        if (!force && null != cacheSummary && addMinutes(cacheSummary.date, properties.cachePeriodInMinutest).after(Date())) {
            return cacheSummary
        }

        val summary = processor.getScaffoldSummary(scaffold)
        cacheSummary?.let { summary.id = it.id }
        val persistSummary = summaryRepository.save(summary)
        shareHolderRepository.deleteAllBySummary(summary)
        val shareHolders = processor.getShareHolders(persistSummary).map { shareHolderRepository.save(it) }
        persistSummary.shareHolders.addAll(shareHolders)
        return persistSummary
    }

    @Transactional
    override fun deactivate(address: String, user: User): ScaffoldSummary {
        val scaffold = get(address, user)
        processor.deactivate(scaffold)
        return getScaffoldSummary(address, user, true)
    }

    private fun stopTrackState(openKey: OpenKey, address: String) {
        if (openKey.stateAccountId == null) return

        stateApi.deleteWallet(openKey.stateAccountId!!, address, Ethereum.getId())
    }

    @Transactional
    override fun activate(address: String, user: User): ScaffoldSummary {
        val scaffold = get(address, user)
        processor.activate(scaffold)
        trackState(scaffold.openKey, scaffold.address, scaffold.webHook)
        return getScaffoldSummary(address, user, true)
    }

    @Transactional
    override fun addShareHolder(address: String, user: User, request: AddShareHolderRequest): ScaffoldSummary {
        val scaffold = get(address, user)
        processor.addShareHolder(scaffold, request.address!!, request.percent!!.toLong())
        return getScaffoldSummary(address, user, true)
    }

    @Transactional
    override fun updateShareHolder(address: String, user: User,
                                   holderAddress: String, request: UpdateShareHolderRequest): ScaffoldSummary {
        val scaffold = get(address, user)
        processor.updateShareHolder(scaffold, holderAddress, request.percent!!.toLong())
        return getScaffoldSummary(address, user, true)
    }

    @Transactional
    override fun removeShareHolder(address: String, user: User, holderAddress: String): ScaffoldSummary {
        val scaffold = get(address, user)
        processor.removeShareHolder(scaffold, holderAddress)
        return getScaffoldSummary(address, user, true)
    }

    private fun trackState(openKey: OpenKey, address: String, webHook: String?) {
        if (openKey.stateAccountId == null) {
            val stateAccount = stateApi.createAccount(webHook, address, Ethereum.getId())
            openKey.stateAccountId = stateAccount.id
            openKeyService.update(openKey)
            return
        }

        stateApi.addWallet(openKey.stateAccountId!!, address, Ethereum.getId())
    }

    private fun updateStateWebHook(openKey: OpenKey, webHook: String) {
        if (openKey.stateAccountId == null) return

        stateApi.updateWebhook(openKey.stateAccountId!!, webHook)
    }

}
