package io.openfuture.api.component.web3

import io.openfuture.api.config.UnitTest
import io.openfuture.api.config.any
import io.openfuture.api.config.anyString
import io.openfuture.api.config.propety.EthereumProperties
import io.openfuture.api.exception.ExecuteTransactionException
import io.openfuture.api.exception.FunctionCallException
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.web3j.abi.TypeReference
import org.web3j.abi.datatypes.generated.Uint256
import org.web3j.crypto.Credentials
import org.web3j.crypto.ECKeyPair
import org.web3j.protocol.Web3j
import org.web3j.protocol.core.DefaultBlockParameter
import org.web3j.protocol.core.DefaultBlockParameterName.LATEST
import org.web3j.protocol.core.Request
import org.web3j.protocol.core.Response
import org.web3j.protocol.core.methods.request.Transaction
import org.web3j.protocol.core.methods.response.*
import java.math.BigInteger
import java.util.*

internal class Web3WrapperTests : UnitTest() {

    @Mock private lateinit var web3j: Web3j
    @Mock private lateinit var properties: EthereumProperties
    @Mock private lateinit var transactionHandler: TransactionHandler

    @Mock private lateinit var call: EthCall
    @Mock private lateinit var error: Response.Error

    @Mock private lateinit var callRequest: Request<Transaction, EthCall>
    @Mock private lateinit var netVersionRequest: Request<String, NetVersion>
    @Mock private lateinit var transactionRequest: Request<String, EthSendTransaction>
    @Mock private lateinit var transactionCountRequest: Request<String, EthGetTransactionCount>
    @Mock private lateinit var transactionReceiptRequest: Request<String, EthGetTransactionReceipt>

    @Mock private lateinit var netVersion: NetVersion
    @Mock private lateinit var transaction: EthSendTransaction
    @Mock private lateinit var transactionCount: EthGetTransactionCount
    @Mock private lateinit var transactionReceipt: EthGetTransactionReceipt
    @Mock private lateinit var credentials: Credentials
    private lateinit var web3Wrapper: Web3Wrapper

    private val addressValue = "0xba37163625b3f2e96112562858c12b75963af138"


    @Before
    fun setUp() {
        web3Wrapper = Web3Wrapper(web3j, properties, transactionHandler)
    }

    @Test
    fun deployTest() {
        val optionalTransactionReceipt = Optional.of(TransactionReceipt().apply { contractAddress = addressValue })

        given(properties.getCredentials()).willReturn(credentials)
        given(credentials.address).willReturn(addressValue)
        given(web3j.ethGetTransactionCount(addressValue, LATEST)).willReturn(transactionCountRequest)
        given(transactionCountRequest.send()).willReturn(transactionCount)
        given(transactionCount.transactionCount).willReturn(BigInteger.ZERO)
        given(web3j.ethSendRawTransaction(anyString())).willReturn(transactionRequest)
        given(credentials.ecKeyPair).willReturn(createECKeyPair())
        given(transactionCount.transactionCount).willReturn(BigInteger.ZERO)
        given(transactionRequest.send()).willReturn(transaction)
        given(transaction.transactionHash).willReturn("hash")
        given(web3j.ethGetTransactionReceipt(transaction.transactionHash)).willReturn(transactionReceiptRequest)
        given(transactionReceiptRequest.send()).willReturn(transactionReceipt)
        given(transactionReceipt.transactionReceipt).willReturn(optionalTransactionReceipt)

        val actualAddress = web3Wrapper.deploy("data", listOf())

        assertThat(actualAddress).isNotEmpty()
        assertThat(actualAddress).isEqualTo(addressValue)
    }

    @Test(expected = ExecuteTransactionException::class)
    fun deployWhenTransactionErrorShouldThrowExceptionTest() {
        given(properties.getCredentials()).willReturn(credentials)
        given(credentials.address).willReturn(addressValue)
        given(credentials.ecKeyPair).willReturn(createECKeyPair())
        given(web3j.ethGetTransactionCount(addressValue, LATEST)).willReturn(transactionCountRequest)
        given(transactionCountRequest.send()).willReturn(transactionCount)
        given(transactionCount.transactionCount).willReturn(BigInteger.ZERO)
        given(web3j.ethSendRawTransaction(anyString())).willReturn(transactionRequest)
        given(transactionRequest.send()).willReturn(transaction)
        given(transaction.hasError()).willReturn(true)
        given(transaction.error).willReturn(error)
        given(error.message).willReturn("error")

        web3Wrapper.deploy("data" ,listOf())
    }

    @Test
    fun callFunctionTest() {
        given(properties.getCredentials()).willReturn(credentials)
        given(credentials.address).willReturn(addressValue)
        given(web3j.ethGetTransactionCount(addressValue, LATEST)).willReturn(transactionCountRequest)
        given(transactionCountRequest.send()).willReturn(transactionCount)
        given(transactionCount.transactionCount).willReturn(BigInteger.ZERO)
        given(web3j.ethCall(any(Transaction::class.java), any(DefaultBlockParameter::class.java))).willReturn(callRequest)
        given(callRequest.send()).willReturn(call)
        given(call.hasError()).willReturn(false)
        given(call.value).willReturn("""0xa5643bf2000000000000000000000000000000000000000000000000000000000000006
                0000000000000000000000000000000000000000000000000000000000000000100000000000000000000000
                000000000000000000000000000000000000000a0000000000000000000000000000000000000000000000000000
                00000000000046461766500000000000000000000000000000000000000000000000000000000000000000000000000
                0000000000000000000000000000000000000000000003000000000000000000000000000000000000000000000000000
                0000000000001000000000000000000000000000000000000000000000000000000000000000200000000
                00000000000000000000000000000000000000000000000000000003""")

        val actualResult = web3Wrapper.callFunction("getShareHolderCount", listOf(),
                listOf(object : TypeReference<Uint256>() {}), addressValue)

        assertThat(actualResult).isNotEmpty
    }

    @Test(expected = FunctionCallException::class)
    fun callFunctionWhenCallErrorShouldThrowExceptionTest() {
        given(properties.getCredentials()).willReturn(credentials)
        given(credentials.address).willReturn(addressValue)
        given(web3j.ethGetTransactionCount(addressValue, LATEST)).willReturn(transactionCountRequest)
        given(transactionCountRequest.send()).willReturn(transactionCount)
        given(transactionCount.transactionCount).willReturn(BigInteger.ZERO)
        given(web3j.ethCall(any(Transaction::class.java), any(DefaultBlockParameter::class.java))).willReturn(callRequest)
        given(callRequest.send()).willReturn(call)
        given(call.hasError()).willReturn(true)
        given(call.error).willReturn(error)
        given(error.message).willReturn("error")

        web3Wrapper.callFunction("getShareHolderCount", listOf(), listOf(object : TypeReference<Uint256>() {}), addressValue)
    }

    @Test
    fun callTransactionTest() {
        val optionalTransactionReceipt = Optional.of(TransactionReceipt().apply { transactionHash = "hash" })

        given(properties.getCredentials()).willReturn(credentials)
        given(credentials.address).willReturn(addressValue)
        given(credentials.ecKeyPair).willReturn(createECKeyPair())
        given(web3j.ethGetTransactionCount(addressValue, LATEST)).willReturn(transactionCountRequest)
        given(transactionCountRequest.send()).willReturn(transactionCount)
        given(transactionCount.transactionCount).willReturn(BigInteger.ZERO)
        given(web3j.ethSendRawTransaction(anyString())).willReturn(transactionRequest)
        given(web3j.ethGetTransactionReceipt(anyString())).willReturn(transactionReceiptRequest)
        given(transactionReceiptRequest.send()).willReturn(transactionReceipt)
        given(transactionRequest.send()).willReturn(transaction)
        given(transaction.transactionHash).willReturn("hash")
        given(transactionReceipt.transactionReceipt).willReturn(optionalTransactionReceipt)

        val actualTransactionalHash = web3Wrapper.callTransaction("deactivate", listOf(), listOf(), addressValue)

        assertThat(actualTransactionalHash).isNotEmpty()
    }

    @Test(expected = ExecuteTransactionException::class)
    fun callTransactionWhenTransactionErrorShouldThrowExceptionTest() {
        given(properties.getCredentials()).willReturn(credentials)
        given(credentials.address).willReturn(addressValue)
        given(credentials.ecKeyPair).willReturn(createECKeyPair())
        given(web3j.ethGetTransactionCount(addressValue, LATEST)).willReturn(transactionCountRequest)
        given(transactionCountRequest.send()).willReturn(transactionCount)
        given(transactionCount.transactionCount).willReturn(BigInteger.ZERO)
        given(web3j.ethSendRawTransaction(anyString())).willReturn(transactionRequest)
        given(transactionRequest.send()).willReturn(transaction)
        given(transaction.hasError()).willReturn(true)
        given(transaction.error).willReturn(error)
        given(error.message).willReturn("error")

        web3Wrapper.callTransaction("deactivate", listOf(), listOf(), addressValue)
    }

    @Test
    fun getNetVersionTest() {
        val version = "version"
        given(web3j.netVersion()).willReturn(netVersionRequest)
        given(netVersionRequest.send()).willReturn(netVersion)
        given(netVersion.netVersion).willReturn(version)

        val actualVersion = web3Wrapper.getNetVersion()

        assertThat(actualVersion).isEqualTo(version)
    }

    private fun createECKeyPair(): ECKeyPair = ECKeyPair(
            BigInteger("70434874820561167833413314465717821912032224559303517602656935329389114250303"),
            BigInteger("8552787867577691478484551058702821390038452396941523688407381362313198396896336414469416" +
                    "913650107925323445523542038836977111416625597658430914944261355576"))

}