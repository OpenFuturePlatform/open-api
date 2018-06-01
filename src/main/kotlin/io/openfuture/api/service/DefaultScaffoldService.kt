package io.openfuture.api.service

import io.openfuture.api.component.ScaffoldCompiler
import io.openfuture.api.component.TransactionHandler
import io.openfuture.api.config.propety.EthereumProperties
import io.openfuture.api.domain.scaffold.DeployScaffoldRequest
import io.openfuture.api.domain.scaffold.ScaffoldSummaryDto
import io.openfuture.api.domain.scaffold.SetWebHookRequest
import io.openfuture.api.entity.auth.User
import io.openfuture.api.entity.scaffold.Scaffold
import io.openfuture.api.entity.scaffold.ScaffoldProperty
import io.openfuture.api.exception.DeployException
import io.openfuture.api.exception.NotFoundException
import io.openfuture.api.repository.ScaffoldPropertyRepository
import io.openfuture.api.repository.ScaffoldRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.web3j.abi.FunctionEncoder
import org.web3j.abi.FunctionReturnDecoder
import org.web3j.abi.TypeReference
import org.web3j.abi.datatypes.Address
import org.web3j.abi.datatypes.Function
import org.web3j.abi.datatypes.Type
import org.web3j.abi.datatypes.Utf8String
import org.web3j.abi.datatypes.generated.Uint256
import org.web3j.crypto.Credentials
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
        private const val ALLOWED_DISABLED_SCAFFOLDS = 10
        private const val GET_SCAFFOLD_SUMMARY_METHOD_NAME = "getScaffoldSummary"
    }


    @PostConstruct
    fun init() {
        web3 = Web3j.build(HttpService(properties.infura))
    }

    @Transactional(readOnly = true)
    override fun getAll(user: User, pageRequest: Pageable): Page<Scaffold> =
            repository.findAllByOpenKeyUser(user, pageRequest)

    @Transactional(readOnly = true)
    override fun get(address: String): Scaffold = repository.findByAddress(address)
            ?: throw NotFoundException("Not found scaffold with address $address")

    @Transactional
    override fun deploy(request: DeployScaffoldRequest): Scaffold {
        if (repository.countByEnabledIsFalse() >= ALLOWED_DISABLED_SCAFFOLDS) {
            throw IllegalStateException("Disabled scaffold count is more than allowed")
        }
        val compiledScaffold = compiler.compile(request.properties)
        val openKey = openKeyService.get(request.openKey!!)
        val encodedConstructor = FunctionEncoder.encodeConstructor(asList<Type<*>>(
                Address(request.developerAddress),
                Utf8String(request.description),
                Utf8String(request.fiatAmount),
                Utf8String(request.currency!!.getValue()),
                Uint256(toWei(request.conversionAmount, ETHER).toBigInteger()))
        )

        val credentials = Credentials.create(properties.privateKey)
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

        val contractAddress = transaction.get().contractAddress
        val scaffold = repository.save(Scaffold.of(contractAddress, openKey, compiledScaffold.abi, request))
        val properties = request.properties.map { propertyRepository.save(ScaffoldProperty.of(scaffold, it)) }
        scaffold.property.addAll(properties)
        return scaffold
    }

    @Transactional
    override fun setWebHook(address: String, request: SetWebHookRequest): Scaffold {
        val scaffold = get(address)

        scaffold.webHook = request.webHook

        return scaffold
    }

    override fun getScaffoldSummary(address: String): ScaffoldSummaryDto {
        val scaffold = get(address)
        val function = Function(
                GET_SCAFFOLD_SUMMARY_METHOD_NAME,
                asList(),
                asList(
                        object : TypeReference<Utf8String>() {},
                        object : TypeReference<Utf8String>() {},
                        object : TypeReference<Utf8String>() {},
                        object : TypeReference<Uint256>() {},
                        object : TypeReference<Uint256>() {},
                        object : TypeReference<Address>() {}
                )
        )
        val encodedFunction = FunctionEncoder.encode(function)

        val credentials = Credentials.create(properties.privateKey)
        val nonce = web3.ethGetTransactionCount(credentials.address, LATEST).send().transactionCount
        val result = web3.ethCall(Transaction.createFunctionCallTransaction(credentials.address, nonce, GAS_PRICE,
                GAS_LIMIT, scaffold.address, encodedFunction), LATEST).send().value

        val decodedResult = FunctionReturnDecoder.decode(result, function.outputParameters)
        return ScaffoldSummaryDto(
                decodedResult[0].value as String,
                decodedResult[2].value as String,
                decodedResult[3].value as String,
                fromWei((decodedResult[4].value as BigInteger).toString(), ETHER),
                decodedResult[5].value as BigInteger,
                decodedResult[6].value as String
        )
    }

}