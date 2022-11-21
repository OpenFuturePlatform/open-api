package io.openfuture.api.component.web3

import io.openfuture.api.config.propety.EthereumProperties
import io.openfuture.api.exception.ExecuteTransactionException
import io.openfuture.api.exception.FunctionCallException
import io.openfuture.api.util.EthereumUtils.toChecksumAddress
import org.springframework.stereotype.Component
import org.web3j.abi.FunctionEncoder
import org.web3j.abi.FunctionReturnDecoder
import org.web3j.abi.TypeReference
import org.web3j.abi.datatypes.Function
import org.web3j.abi.datatypes.Type
import org.web3j.crypto.Credentials
import org.web3j.crypto.RawTransaction
import org.web3j.crypto.RawTransaction.createContractTransaction
import org.web3j.crypto.RawTransaction.createTransaction
import org.web3j.crypto.TransactionEncoder.signMessage
import org.web3j.protocol.Web3j
import org.web3j.protocol.core.DefaultBlockParameterName.LATEST
import org.web3j.protocol.core.methods.request.Transaction.createFunctionCallTransaction
import org.web3j.protocol.core.methods.response.TransactionReceipt
import org.web3j.tx.Contract.GAS_LIMIT
import org.web3j.tx.ManagedTransaction.GAS_PRICE
import org.web3j.utils.Numeric.toHexString
import java.math.BigInteger
import java.math.BigInteger.ZERO

@Component
class Web3Wrapper(
        private val web3j: Web3j,
        private val properties: EthereumProperties
) {

    fun deploy(bin: String?, constructorParams: List<Type<*>>): String {
        val encodedConstructor = FunctionEncoder.encodeConstructor(constructorParams)
        val credentials = properties.getCredentials()
        val transaction = createContractTransaction(getNonce(credentials.address), GAS_PRICE, GAS_LIMIT, ZERO,
                bin + encodedConstructor)
        val result = executeTransaction(transaction, credentials)
        return toChecksumAddress(result.contractAddress)
    }

    fun callFunction(methodName: String, inputParams: List<Type<*>>, outputParams: List<TypeReference<*>>,
                     address: String): MutableList<Type<Any>> {
        val function = Function(methodName, inputParams, outputParams)
        val encodedFunction = FunctionEncoder.encode(function)
        val credentials = properties.getCredentials()
        val result = web3j.ethCall(createFunctionCallTransaction(credentials.address, getNonce(credentials.address),
                GAS_PRICE, GAS_LIMIT, address, encodedFunction), LATEST).send()

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
        val transaction = createTransaction(getNonce(credentials.address), GAS_PRICE, GAS_LIMIT, address,
                encodedFunction)
        return executeTransaction(transaction, credentials).transactionHash
    }

    fun getNonce(address: String): BigInteger = web3j.ethGetTransactionCount(address, LATEST).send().transactionCount

    fun getNetVersion(): String = web3j.netVersion().send().netVersion

    fun broadcastTransaction(signedTransaction: String): TransactionReceipt {
        return sendRawTransaction("0x$signedTransaction")
    }

    private fun executeTransaction(transaction: RawTransaction, credentials: Credentials): TransactionReceipt {
        val encodedTransaction = signMessage(transaction, credentials)

        return sendRawTransaction(toHexString(encodedTransaction))
    }

    private fun sendRawTransaction(signedTransaction: String): TransactionReceipt {
        val result = web3j.ethSendRawTransaction(signedTransaction).send()

        if (result.hasError()) {
            throw ExecuteTransactionException(result.error.message)
        }

        while (!web3j.ethGetTransactionReceipt(result.transactionHash).send().transactionReceipt.isPresent) {
            Thread.sleep(1000)
        }

        return web3j.ethGetTransactionReceipt(result.transactionHash).send().transactionReceipt.get()
    }

}