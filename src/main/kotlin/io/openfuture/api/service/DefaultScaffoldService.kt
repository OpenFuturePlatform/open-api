package io.openfuture.api.service

import io.openfuture.api.component.ScaffoldCompiler
import io.openfuture.api.component.TransactionHandler
import io.openfuture.api.config.propety.EthereumProperties
import io.openfuture.api.domain.scaffold.*
import io.openfuture.api.entity.auth.User
import io.openfuture.api.entity.scaffold.Scaffold
import io.openfuture.api.entity.scaffold.ScaffoldProperty
import io.openfuture.api.exception.*
import io.openfuture.api.repository.ScaffoldPropertyRepository
import io.openfuture.api.repository.ScaffoldRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.web3j.abi.*
import org.web3j.abi.datatypes.*
import org.web3j.abi.datatypes.Function
import org.web3j.abi.datatypes.generated.Uint256
import org.web3j.crypto.RawTransaction
import org.web3j.crypto.TransactionEncoder
import org.web3j.protocol.Web3j
import org.web3j.protocol.core.DefaultBlockParameterName.LATEST
import org.web3j.protocol.core.methods.request.Transaction
import org.web3j.protocol.http.HttpService
import org.web3j.tx.gas.DefaultGasProvider.GAS_LIMIT
import org.web3j.tx.gas.DefaultGasProvider.GAS_PRICE
import org.web3j.utils.Convert.Unit.ETHER
import org.web3j.utils.Convert.fromWei
import org.web3j.utils.Convert.toWei
import org.web3j.utils.Numeric
import java.math.BigInteger
import java.math.BigInteger.ZERO
import java.util.Arrays.asList
import javax.annotation.PostConstruct


/**
 * @author Kadach Alexey
 */
@Service
class DefaultScaffoldService(
        private val repository: ScaffoldRepository,
        private val propertyRepository: ScaffoldPropertyRepository,
        private val compiler: ScaffoldCompiler,
        private val properties: EthereumProperties,
        private val openKeyService: OpenKeyService,
        private val transactionHandler: TransactionHandler
) : ScaffoldService {

    private lateinit var web3: Web3j

    companion object {
        private const val ALLOWED_DISABLED_SCAFFOLDS = 10L
        private const val ENABLED_SCAFFOLD_TOKEN_COUNT = 10L
        private const val GET_SCAFFOLD_SUMMARY_METHOD_NAME = "getScaffoldSummary"
        private const val DEACTIVATE_SCAFFOLD_METHOD_NAME = "deactivate"
    }


    @PostConstruct
    fun init() {
        web3 = Web3j.build(HttpService(properties.infura))
        web3.transactionObservable().subscribe {
            val transactionReceipt = web3.ethGetTransactionReceipt(it.hash).send().transactionReceipt
            if (transactionReceipt.isPresent) {
                transactionReceipt.get().logs.forEach {
                    transactionHandler.handle(it)
                }
            }
        }
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
        if (repository.countByEnabledIsFalseAndOpenKeyUser(openKey.user) >= ALLOWED_DISABLED_SCAFFOLDS) {
            throw IllegalStateException("Disabled scaffold count is more than allowed")
        }

        val compiledScaffold = compiler.compile(request.properties)
        return CompiledScaffoldDto(compiledScaffold)
    }

    @Transactional
    override fun deploy(request: DeployScaffoldRequest): Scaffold {
        val compiledScaffold = compile(CompileScaffoldRequest(request.openKey, request.properties))
        val encodedConstructor = FunctionEncoder.encodeConstructor(asList<Type<*>>(
                Address(request.developerAddress),
                Utf8String(request.description),
                Utf8String(request.fiatAmount),
                Utf8String(request.currency!!.getValue()),
                Uint256(toWei(request.conversionAmount, ETHER).toBigInteger()))
        )

        val credentials = properties.getCredentials()
        val nonce = web3.ethGetTransactionCount(credentials.address, LATEST).send().transactionCount
        val rawTransaction = RawTransaction.createContractTransaction(nonce, GAS_PRICE, GAS_LIMIT, ZERO,
                compiledScaffold.bin + encodedConstructor)
        val encodedTransaction = TransactionEncoder.signMessage(rawTransaction, credentials)
        val deployResult = web3.ethSendRawTransaction(Numeric.toHexString(encodedTransaction)).send()
        if (deployResult.hasError()) {
            throw DeployException(deployResult.error.message)
        }

        while (!web3.ethGetTransactionReceipt(deployResult.transactionHash).send().transactionReceipt.isPresent) {
            Thread.sleep(1000)
        }

        val transaction = web3.ethGetTransactionReceipt(deployResult.transactionHash).send().transactionReceipt

        return save(SaveScaffoldRequest(
                transaction.get().contractAddress,
                compiledScaffold.abi,
                request.openKey,
                request.developerAddress,
                request.description,
                request.fiatAmount,
                request.currency,
                request.conversionAmount,
                request.properties
        ))
    }

    @Transactional
    override fun save(request: SaveScaffoldRequest): Scaffold {
        val openKey = openKeyService.get(request.openKey!!)
        val scaffold = repository.save(Scaffold.of(request, openKey))
        val properties = request.properties.map { propertyRepository.save(ScaffoldProperty.of(scaffold, it)) }
        scaffold.property.addAll(properties)
        return scaffold
    }

    @Transactional
    override fun setWebHook(address: String, request: SetWebHookRequest, user: User): Scaffold {
        val scaffold = get(address, user)

        scaffold.webHook = request.webHook

        return scaffold
    }

    @Transactional(readOnly = true)
    override fun getScaffoldSummary(address: String, user: User): ScaffoldSummaryDto {
        val scaffold = get(address, user)
        val function = Function(
                GET_SCAFFOLD_SUMMARY_METHOD_NAME,
                asList(),
                asList(
                        object : TypeReference<Utf8String>() {},
                        object : TypeReference<Utf8String>() {},
                        object : TypeReference<Utf8String>() {},
                        object : TypeReference<Uint256>() {},
                        object : TypeReference<Uint256>() {},
                        object : TypeReference<Address>() {},
                        object : TypeReference<Uint256>() {}
                )
        )

        val result = callFunction(function, scaffold.address)
        return ScaffoldSummaryDto(
                result[0].value as String,
                result[1].value as String,
                result[2].value as String,
                fromWei((result[3].value as BigInteger).toBigDecimal(), ETHER),
                result[4].value as BigInteger,
                result[5].value as String,
                result[6].value as BigInteger,
                result[6].value as BigInteger >= BigInteger.valueOf(ENABLED_SCAFFOLD_TOKEN_COUNT)
        )
    }

    @Transactional(readOnly = true)
    override fun deactivate(address: String, user: User): ScaffoldSummaryDto {
        val scaffold = get(address, user)
        val function = Function(
                DEACTIVATE_SCAFFOLD_METHOD_NAME,
                asList(),
                asList()
        )

        callFunction(function, scaffold.address)
        return getScaffoldSummary(scaffold.address, user)
    }

    @Transactional(readOnly = true)
    override fun getQuota(user: User): ScaffoldQuotaDto {
        val scaffoldCount = repository.countByEnabledIsFalseAndOpenKeyUser(user)
        return ScaffoldQuotaDto(scaffoldCount, ENABLED_SCAFFOLD_TOKEN_COUNT)
    }

    private fun callFunction(function: Function, address: String): MutableList<Type<Any>> {
        val encodedFunction = FunctionEncoder.encode(function)
        val credentials = properties.getCredentials()
        val nonce = web3.ethGetTransactionCount(credentials.address, LATEST).send().transactionCount
        val result = web3.ethCall(Transaction.createFunctionCallTransaction(credentials.address, nonce, GAS_PRICE,
                GAS_LIMIT, address, encodedFunction), LATEST).send()

        if (result.hasError()) {
            throw FunctionCallException(result.error.message)
        }

        return FunctionReturnDecoder.decode(result.value, function.outputParameters)
    }

}