package io.openfuture.api.service

import io.openfuture.api.component.ScaffoldCompiler
import io.openfuture.api.component.TransactionHandler
import io.openfuture.api.config.propety.BlockchainProperties
import io.openfuture.api.domain.scaffold.DeployScaffoldRequest
import io.openfuture.api.entity.auth.User
import io.openfuture.api.entity.scaffold.Scaffold
import io.openfuture.api.entity.scaffold.ScaffoldProperty
import io.openfuture.api.exception.DeployException
import io.openfuture.api.exception.NotFoundException
import io.openfuture.api.repository.ScaffoldPropertyRepository
import io.openfuture.api.repository.ScaffoldRepository
import io.openfuture.api.util.HexUtils
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.web3j.abi.FunctionEncoder
import org.web3j.abi.datatypes.Address
import org.web3j.abi.datatypes.Type
import org.web3j.abi.datatypes.Utf8String
import org.web3j.abi.datatypes.generated.Uint256
import org.web3j.protocol.Web3j
import org.web3j.protocol.core.DefaultBlockParameterName.EARLIEST
import org.web3j.protocol.core.DefaultBlockParameterName.LATEST
import org.web3j.protocol.core.methods.request.EthFilter
import org.web3j.protocol.core.methods.request.Transaction.createContractTransaction
import org.web3j.protocol.http.HttpService
import org.web3j.tx.gas.DefaultGasProvider.GAS_LIMIT
import org.web3j.tx.gas.DefaultGasProvider.GAS_PRICE
import org.web3j.utils.Convert.Unit.WEI
import org.web3j.utils.Convert.toWei
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
        private val properties: BlockchainProperties,
        private val openKeyService: OpenKeyService,
        private val transactionHandler: TransactionHandler
) : ScaffoldService {

    private lateinit var web3: Web3j

    companion object {
        private const val ALLOWED_DISABLED_SCAFFOLDS = 10
    }


    @PostConstruct
    fun init() {
        web3 = Web3j.build(HttpService(properties.url))
        repository.findAll().forEach {
            val filter = EthFilter(EARLIEST, LATEST, HexUtils.decode(it.address))
            web3.ethLogObservable(filter).subscribe {
                transactionHandler.handle(it)
            }
        }
    }

    @Transactional(readOnly = true)
    override fun getAll(user: User, pageRequest: Pageable): Page<Scaffold> =
            repository.findAllByOpenKeyUser(user, pageRequest)

    @Transactional(readOnly = true)
    override fun get(address: String): Scaffold = repository.findByAddress(address)
            ?: throw NotFoundException("Not found scaffold with address $address")

    @Transactional
    override fun deploy(request: DeployScaffoldRequest, user: User): Scaffold {
        if (repository.countByEnabledIsFalse() >= ALLOWED_DISABLED_SCAFFOLDS) {
            throw IllegalStateException("Disabled scaffold count is more than allowed")
        }
        val compiledScaffold = compiler.compile(request.properties)
        val openKey = openKeyService.get(request.openKey!!, user)
        val encodedConstructor = FunctionEncoder.encodeConstructor(asList<Type<*>>(
                Address(request.developerAddress),
                Utf8String(request.description),
                Utf8String(request.fiatAmount),
                Utf8String(request.currency!!.getValue()),
                Uint256(toWei(request.conversionAmount, WEI).toBigInteger()))
        )

        val nonce = web3.ethGetTransactionCount(properties.baseAccount, LATEST).send().transactionCount
        val deployResult = web3.ethSendTransaction(createContractTransaction(properties.baseAccount, nonce,
                GAS_PRICE, GAS_LIMIT, ZERO, compiledScaffold.bin + encodedConstructor)).send()
        if (deployResult.hasError()) {
            throw DeployException(deployResult.error.message)
        }

        val transaction = web3.ethGetTransactionReceipt(deployResult.transactionHash).send().transactionReceipt
        if (!transaction.isPresent) {
            throw DeployException("Can't get contract address")
        }

        val contractAddress = transaction.get().contractAddress
        val filter = EthFilter(EARLIEST, LATEST, HexUtils.decode(contractAddress))
        web3.ethLogObservable(filter).subscribe {
            transactionHandler.handle(it)
        }

        val scaffold = repository.save(Scaffold.of(contractAddress, openKey, compiledScaffold.abi, request))
        val properties = request.properties.map { propertyRepository.save(ScaffoldProperty.of(scaffold, it)) }
        scaffold.property.addAll(properties)
        return scaffold
    }

}