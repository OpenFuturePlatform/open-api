package io.zensoft.open.api.service

import io.zensoft.open.api.component.ScaffoldCompiler
import io.zensoft.open.api.config.propety.BlockchainProperties
import io.zensoft.open.api.domain.scaffold.DeployScaffoldRequest
import io.zensoft.open.api.exception.DeployException
import io.zensoft.open.api.exception.NotFoundException
import io.zensoft.open.api.model.Scaffold
import io.zensoft.open.api.model.User
import io.zensoft.open.api.repository.ScaffoldRepository
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
import org.web3j.protocol.core.DefaultBlockParameterName.LATEST
import org.web3j.protocol.core.methods.request.Transaction.createContractTransaction
import org.web3j.protocol.http.HttpService
import org.web3j.tx.gas.DefaultGasProvider.GAS_LIMIT
import org.web3j.tx.gas.DefaultGasProvider.GAS_PRICE
import org.web3j.utils.Convert.Unit.WEI
import org.web3j.utils.Convert.toWei
import java.math.BigInteger.ZERO
import java.util.Arrays.asList


/**
 * @author Kadach Alexey
 */
@Service
class DefaultScaffoldService(
        private val repository: ScaffoldRepository,
        private val compiler: ScaffoldCompiler,
        private val properties: BlockchainProperties,
        private val openKeyService: OpenKeyService
) : ScaffoldService {

    @Transactional(readOnly = true)
    override fun getAll(user: User, pageRequest: Pageable): Page<Scaffold> = repository.findAllByUser(user, pageRequest)

    @Transactional(readOnly = true)
    override fun get(address: String): Scaffold = repository.findByAddress(address)
            ?: throw NotFoundException("Not found scaffold with address $address")

    @Transactional
    override fun deploy(request: DeployScaffoldRequest, user: User): Scaffold {
        val compiledScaffold = compiler.compileScaffold(request.scaffoldFields)
        val openKey = openKeyService.get(request.openKey!!, user)
        val encodedConstructor = FunctionEncoder.encodeConstructor(asList<Type<*>>(
                Address(request.developerAddress),
                Utf8String(request.scaffoldDescription),
                Utf8String(request.fiatAmount!!),
                Utf8String(request.conversionCurrency!!.getValue()),
                Uint256(toWei(request.currencyConversionValue!!, WEI).toBigInteger()))
        )
        val web3 = Web3j.build(HttpService(properties.url))


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

        return repository.save(Scaffold(transaction.get().contractAddress, user, openKey, compiledScaffold.abi))
    }

}