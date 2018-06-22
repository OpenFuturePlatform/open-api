package io.openfuture.api.component.web3

import io.openfuture.api.config.propety.EthereumProperties
import io.openfuture.api.exception.DeployException
import io.openfuture.api.exception.FunctionCallException
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.web3j.abi.FunctionEncoder
import org.web3j.abi.FunctionReturnDecoder
import org.web3j.abi.TypeReference
import org.web3j.abi.datatypes.Function
import org.web3j.abi.datatypes.Type
import org.web3j.crypto.RawTransaction.createContractTransaction
import org.web3j.crypto.TransactionEncoder.signMessage
import org.web3j.protocol.Web3j
import org.web3j.protocol.core.DefaultBlockParameterName.LATEST
import org.web3j.protocol.core.methods.request.Transaction.createFunctionCallTransaction
import org.web3j.tx.Contract.GAS_LIMIT
import org.web3j.tx.ManagedTransaction.GAS_PRICE
import org.web3j.utils.Numeric.toHexString
import java.math.BigInteger.ZERO
import javax.annotation.PostConstruct

@Component
class Web3Wrapper(
        private val web3j: Web3j,
        private val properties: EthereumProperties,
        private val transactionHandler: TransactionHandler
) {

    companion object {
        private val log = LoggerFactory.getLogger(Web3Wrapper::class.java)
    }


    @PostConstruct
    fun init() {
        try {
            web3j.transactionObservable().subscribe {
                val transactionReceipt = web3j.ethGetTransactionReceipt(it.hash).send().transactionReceipt
                if (transactionReceipt.isPresent) {
                    transactionReceipt.get().logs.forEach {
                        transactionHandler.handle(it)
                    }
                }
            }
        } catch (e: Exception) {
            log.warn(e.message)
        }
    }

    fun deploy(data: String): String {
        val credentials = properties.getCredentials()
        val nonce = web3j.ethGetTransactionCount(credentials.address, LATEST).send().transactionCount
        val rawTransaction = createContractTransaction(nonce, GAS_PRICE, GAS_LIMIT, ZERO, data)
        val encodedTransaction = signMessage(rawTransaction, credentials)
        val deployResult = web3j.ethSendRawTransaction(toHexString(encodedTransaction)).send()

        if (deployResult.hasError()) {
            throw DeployException(deployResult.error.message)
        }

        while (!web3j.ethGetTransactionReceipt(deployResult.transactionHash).send().transactionReceipt.isPresent) {
            Thread.sleep(1000)
        }

        val transactionReceipt = web3j.ethGetTransactionReceipt(deployResult.transactionHash).send().transactionReceipt
        return transactionReceipt.get().contractAddress
    }

    fun callFunction(methodName: String, inputParams: List<Type<*>>, outputParams: List<TypeReference<*>>,
                     address: String): MutableList<Type<Any>> {
        val function = Function(methodName, inputParams, outputParams)
        val encodedFunction = FunctionEncoder.encode(function)
        val credentials = properties.getCredentials()
        val nonce = web3j.ethGetTransactionCount(credentials.address, LATEST).send().transactionCount
        val result = web3j.ethCall(createFunctionCallTransaction(credentials.address, nonce, GAS_PRICE, GAS_LIMIT,
                address, encodedFunction), LATEST).send()

        if (result.hasError()) {
            throw FunctionCallException(result.error.message)
        }

        return FunctionReturnDecoder.decode(result.value, function.outputParameters)
    }

    fun callTransaction(methodName: String, inputParams: List<Type<*>>, outputParams: List<TypeReference<*>>,
                        address: String): String {
        val function = Function(methodName, inputParams, outputParams)
        val encodedFunction = FunctionEncoder.encode(function)
        val credentials = properties.getCredentials()
        val nonce = web3j.ethGetTransactionCount(credentials.address, LATEST).send().transactionCount
        val result = web3j.ethSendTransaction(createFunctionCallTransaction(credentials.address, nonce, GAS_PRICE,
                GAS_LIMIT, address, encodedFunction)).send()

        if (result.hasError()) {
            throw FunctionCallException(result.error.message)
        }

        return result.transactionHash
    }

    fun getNetVersion(): String = web3j.netVersion().send().netVersion

}