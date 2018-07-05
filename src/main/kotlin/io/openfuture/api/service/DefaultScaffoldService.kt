package io.openfuture.api.service

import io.openfuture.api.component.scaffold.ScaffoldCompiler
import io.openfuture.api.component.web3.Web3Wrapper
import io.openfuture.api.config.propety.EthereumProperties
import io.openfuture.api.domain.scaffold.*
import io.openfuture.api.entity.auth.User
import io.openfuture.api.entity.scaffold.Scaffold
import io.openfuture.api.entity.scaffold.ScaffoldProperty
import io.openfuture.api.entity.scaffold.ScaffoldSummary
import io.openfuture.api.entity.scaffold.ShareHolder
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
import org.web3j.abi.TypeReference
import org.web3j.abi.datatypes.Address
import org.web3j.abi.datatypes.Utf8String
import org.web3j.abi.datatypes.generated.Bytes32
import org.web3j.abi.datatypes.generated.Uint256
import org.web3j.utils.Convert.Unit.ETHER
import org.web3j.utils.Convert.toWei
import java.math.BigInteger
import java.util.*

@Service
class DefaultScaffoldService(
        private val web3: Web3Wrapper,
        private val repository: ScaffoldRepository,
        private val propertyRepository: ScaffoldPropertyRepository,
        private val summaryRepository: ScaffoldSummaryRepository,
        private val shareHolderRepository: ShareHolderRepository,
        private val compiler: ScaffoldCompiler,
        private val properties: EthereumProperties,
        private val openKeyService: OpenKeyService
) : ScaffoldService {

    companion object {
        private const val GET_SCAFFOLD_SUMMARY_METHOD_NAME = "getScaffoldSummary"
        private const val DEACTIVATE_SCAFFOLD_METHOD_NAME = "deactivate"
        private const val ADD_SHARE_HOLDER_METHOD_NAME = "addShareHolder"
        private const val UPDATE_SHARE_HOLDER_METHOD_NAME = "editShareHolder"
        private const val REMOVE_SHARE_HOLDER_METHOD_NAME = "deleteShareHolder"
        private const val GET_SHARE_HOLDER_NUMBER_METHOD_NAME = "getShareHolderCount"
        private const val GET_SHARE_HOLDER_AT_INDEX_METHOD_NAME = "getShareHolderAddressAndShareAtIndex"
    }

    @Transactional(readOnly = true)
    override fun getAll(user: User, pageRequest: Pageable): Page<Scaffold> =
            repository.findAllByOpenKeyUser(user, pageRequest)

    @Transactional(readOnly = true)
    override fun get(address: String, user: User): Scaffold = repository.findByAddressAndOpenKeyUser(address, user)
            ?: throw NotFoundException("Not found scaffold with address $address")

    @Transactional(readOnly = true)
    override fun compile(request: CompileScaffoldRequest): CompiledScaffoldDto {
        val openKey = openKeyService.get(request.openKey!!)
        if (summaryRepository.countByEnabledIsFalseAndScaffoldOpenKeyUser(openKey.user) >= properties.allowedDisabledContracts) {
            throw IllegalStateException("Disabled scaffold count is more than allowed")
        }

        val compiledScaffold = compiler.compile(request.properties)
        return CompiledScaffoldDto(compiledScaffold)
    }

    @Transactional
    override fun deploy(request: DeployScaffoldRequest): Scaffold {
        val compiledScaffold = compile(CompileScaffoldRequest(request.openKey, request.properties))
        val credentials = properties.getCredentials()
        val contactAddress = web3.deploy(
                compiledScaffold.bin,
                listOf(
                        Address(request.developerAddress),
                        Address(credentials.address),
                        Utf8String(request.fiatAmount),
                        Utf8String(request.currency!!.getValue()),
                        Uint256(toWei(request.conversionAmount, ETHER).toBigInteger())
                )
        )

        return save(SaveScaffoldRequest(
                contactAddress,
                compiledScaffold.abi,
                request.openKey,
                request.developerAddress,
                request.description,
                request.fiatAmount,
                request.currency,
                request.conversionAmount,
                request.webHook,
                request.properties
        ))
    }

    @Transactional
    override fun save(request: SaveScaffoldRequest): Scaffold {
        val openKey = openKeyService.get(request.openKey!!)
        val scaffold = repository.save(Scaffold.of(request, openKey))
        val properties = request.properties.map { propertyRepository.save(ScaffoldProperty.of(scaffold, it)) }
        scaffold.property.addAll(properties)
        getScaffoldSummary(scaffold)
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

        return repository.save(scaffold)
    }

    @Transactional(readOnly = true)
    override fun getQuota(user: User): ScaffoldQuotaDto {
        val scaffoldCount = summaryRepository.countByEnabledIsFalseAndScaffoldOpenKeyUser(user)
        return ScaffoldQuotaDto(scaffoldCount, properties.enabledContactTokenCount)
    }

    @Transactional
    override fun getScaffoldSummary(address: String, user: User, cache: Boolean): ScaffoldSummary {
        val scaffold = get(address, user)
        val cacheSummary = summaryRepository.findByScaffold(scaffold)
        if (null != cacheSummary && addMinutes(cacheSummary.date, properties.cachePeriodInMinutest).after(Date()) && cache) {
            return cacheSummary
        }

        val summary = getScaffoldSummary(scaffold)
        cacheSummary?.let { summary.id = it.id }
        val persistSummary = summaryRepository.save(summary)
        shareHolderRepository.deleteAllBySummary(summary)
        val shareHolders = getShareHolders(persistSummary).map { shareHolderRepository.save(it) }
        persistSummary.shareHolders.addAll(shareHolders)
        return persistSummary
    }

    @Transactional
    override fun deactivate(address: String, user: User): ScaffoldSummary {
        val scaffold = get(address, user)
        web3.callTransaction(DEACTIVATE_SCAFFOLD_METHOD_NAME, listOf(), listOf(), scaffold.address)
        return getScaffoldSummary(address, user, false)
    }

    @Transactional
    override fun addShareHolder(address: String, user: User, request: AddShareHolderRequest): ScaffoldSummary {
        val scaffold = get(address, user)
        web3.callTransaction(ADD_SHARE_HOLDER_METHOD_NAME, listOf(Address(request.address),
                Uint256(request.percent.toLong())), listOf(), scaffold.address)
        return getScaffoldSummary(address, user, false)
    }

    @Transactional
    override fun updateShareHolder(address: String, user: User, request: UpdateShareHolderRequest): ScaffoldSummary {
        val scaffold = get(address, user)
        web3.callTransaction(UPDATE_SHARE_HOLDER_METHOD_NAME, listOf(Address(request.address),
                Uint256(request.percent.toLong())), listOf(), scaffold.address)
        return getScaffoldSummary(address, user, false)
    }

    @Transactional
    override fun removeShareHolder(address: String, user: User, request: RemoveShareHolderRequest): ScaffoldSummary {
        val scaffold = get(address, user)
        web3.callTransaction(REMOVE_SHARE_HOLDER_METHOD_NAME, listOf(Address(request.address)), listOf(),
                scaffold.address)
        return getScaffoldSummary(address, user, false)
    }

    private fun getScaffoldSummary(scaffold: Scaffold): ScaffoldSummary {
        val result = web3.callFunction(
                GET_SCAFFOLD_SUMMARY_METHOD_NAME,
                listOf(),
                listOf(
                        object : TypeReference<Bytes32>() {},
                        object : TypeReference<Bytes32>() {},
                        object : TypeReference<Uint256>() {},
                        object : TypeReference<Uint256>() {},
                        object : TypeReference<Address>() {},
                        object : TypeReference<Uint256>() {}
                ),
                scaffold.address
        )

        return ScaffoldSummary(
                scaffold,
                result[3].value as BigInteger,
                result[5].value as BigInteger,
                result[5].value as BigInteger >= BigInteger.valueOf(properties.enabledContactTokenCount.toLong())
        )
    }

    private fun getShareHolders(summary: ScaffoldSummary): List<ShareHolder> {
        val countResult = web3.callFunction(
                GET_SHARE_HOLDER_NUMBER_METHOD_NAME,
                listOf(),
                listOf(object : TypeReference<Uint256>() {}),
                summary.scaffold.address
        )
        val count = (countResult[0].value as BigInteger).toInt()

        val shareHolders = mutableListOf<ShareHolder>()
        for (i in 0 until count) {
            val shareHolderResult = web3.callFunction(
                    GET_SHARE_HOLDER_AT_INDEX_METHOD_NAME,
                    listOf(Uint256(i.toLong())),
                    listOf(object : TypeReference<Address>() {}, object : TypeReference<Uint256>() {}),
                    summary.scaffold.address
            )
            shareHolders.add(ShareHolder(
                    summary,
                    shareHolderResult[0].value as String,
                    (shareHolderResult[1].value as BigInteger).toInt()
            ))
        }

        return shareHolders
    }

}