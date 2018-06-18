package io.openfuture.api.service

import io.openfuture.api.component.ScaffoldCompiler
import io.openfuture.api.component.TransactionHandler
import io.openfuture.api.config.*
import io.openfuture.api.config.propety.EthereumProperties
import io.openfuture.api.domain.scaffold.*
import io.openfuture.api.entity.auth.OpenKey
import io.openfuture.api.entity.auth.User
import io.openfuture.api.entity.scaffold.Currency
import io.openfuture.api.entity.scaffold.PropertyType
import io.openfuture.api.entity.scaffold.Scaffold
import io.openfuture.api.entity.scaffold.ScaffoldProperty
import io.openfuture.api.exception.DeployException
import io.openfuture.api.exception.FunctionCallException
import io.openfuture.api.exception.NotFoundException
import io.openfuture.api.repository.ScaffoldPropertyRepository
import io.openfuture.api.repository.ScaffoldRepository
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.ethereum.solidity.compiler.CompilationResult
import org.junit.Before
import org.junit.Test
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.web3j.crypto.Credentials
import org.web3j.crypto.ECKeyPair
import org.web3j.protocol.Web3j
import org.web3j.protocol.core.DefaultBlockParameter
import org.web3j.protocol.core.DefaultBlockParameterName
import org.web3j.protocol.core.Request
import org.web3j.protocol.core.Response
import org.web3j.protocol.core.methods.request.Transaction
import org.web3j.protocol.core.methods.response.*
import java.math.BigInteger
import java.util.*

internal class DefaultScaffoldServiceTests : UnitTest() {

    @Mock private lateinit var compiler: ScaffoldCompiler
    @Mock private lateinit var repository: ScaffoldRepository
    @Mock private lateinit var properties: EthereumProperties
    @Mock private lateinit var openKeyService: OpenKeyService
    @Mock private lateinit var transactionHandler: TransactionHandler
    @Mock private lateinit var propertyRepository: ScaffoldPropertyRepository

    @Mock private lateinit var web3j: Web3j
    @Mock private lateinit var call: EthCall
    @Mock private lateinit var error: Response.Error

    @Mock private lateinit var callRequest: Request<Transaction, EthCall>
    @Mock private lateinit var transactionRequest: Request<String, EthSendTransaction>
    @Mock private lateinit var transactionCountRequest: Request<String, EthGetTransactionCount>
    @Mock private lateinit var transactionReceiptRequest: Request<String, EthGetTransactionReceipt>

    @Mock private lateinit var transaction: EthSendTransaction
    @Mock private lateinit var transactionCount: EthGetTransactionCount
    @Mock private lateinit var transactionReceipt: EthGetTransactionReceipt

    @Mock private lateinit var pageable: Pageable
    @Mock private lateinit var credentials: Credentials

    private lateinit var service: ScaffoldService

    private val user = User(GOOGLE_ID)


    @Before
    fun setUp() {
        service = DefaultScaffoldService(web3j, repository, propertyRepository, compiler, properties, openKeyService, transactionHandler)
    }

    @Test
    fun getAll() {
        val expectedScaffoldPages = PageImpl(Collections.singletonList(getScaffold()), pageable, 1)

        given(repository.findAllByOpenKeyUser(user, pageable)).willReturn(expectedScaffoldPages)

        val actualScaffoldPages = service.getAll(user, pageable)

        Assertions.assertThat(actualScaffoldPages).isEqualTo(expectedScaffoldPages)
    }

    @Test
    fun get() {
        val expectedScaffold = getScaffold()
        given(repository.findByAddressAndOpenKeyUser(ADDRESS_VALUE, user)).willReturn(expectedScaffold)

        val actualScaffold = service.get(ADDRESS_VALUE, user)

        Assertions.assertThat(actualScaffold).isEqualTo(expectedScaffold)
    }

    @Test(expected = NotFoundException::class)
    fun getWithNotFoundException() {
        given(repository.findByAddressAndOpenKeyUser(ADDRESS_VALUE, user)).willReturn(null)

        service.get(ADDRESS_VALUE, user)
    }


    @Test
    fun compile() {
        val openKey = OpenKey(user)
        val request = CompileScaffoldRequest(OPEN_KEY_VALUE)
        val contractMetadata = CompilationResult.ContractMetadata().apply { abi = "abi"; bin = "bin" }
        val expectedContractMetadata = CompiledScaffoldDto(contractMetadata)

        given(openKeyService.get(OPEN_KEY_VALUE)).willReturn(openKey)
        given(repository.countByEnabledIsFalseAndOpenKeyUser(user)).willReturn(1)
        given(compiler.compile(request.properties)).willReturn(contractMetadata)

        val actualContractMetadata = service.compile(request)

        assertThat(actualContractMetadata).isEqualTo(expectedContractMetadata)
    }

    @Test(expected = IllegalStateException::class)
    fun compileWithExceedingNumberOfAvailableDisabledScaffolds() {
        val openKey = OpenKey(user)
        val request = CompileScaffoldRequest(OPEN_KEY_VALUE)

        given(openKeyService.get(OPEN_KEY_VALUE)).willReturn(openKey)
        given(repository.countByEnabledIsFalseAndOpenKeyUser(user)).willReturn(11)

        service.compile(request)
    }

    @Test
    fun deploy() {
        val expectedScaffold = getScaffold()
        val scaffoldPropertyDto = ScaffoldPropertyDto("name", PropertyType.STRING, "value")
        val request = DeployScaffoldRequest(OPEN_KEY_VALUE, ADDRESS_VALUE, "description", "1", Currency.USD, "1", listOf(scaffoldPropertyDto))
        val optionalTransactionReceipt = Optional.of(TransactionReceipt().apply { contractAddress = ADDRESS_VALUE })
        val contractMetadata = CompilationResult.ContractMetadata().apply { abi = "abi"; bin = "bin" }

        mockDeploy()

        given(repository.save(any(Scaffold::class.java))).will {invocation -> invocation.arguments[0] }
        given(propertyRepository.save(any(ScaffoldProperty::class.java))).will {invocation -> invocation.arguments[0] }
        given(openKeyService.get(OPEN_KEY_VALUE)).willReturn(expectedScaffold.openKey)
        given(compiler.compile(request.properties)).willReturn(contractMetadata)
        given(transaction.hasError()).willReturn(false)
        given(web3j.ethGetTransactionReceipt(transaction.transactionHash)).willReturn(transactionReceiptRequest)
        given(transactionReceiptRequest.send()).willReturn(transactionReceipt)
        given(transactionReceipt.transactionReceipt).willReturn(optionalTransactionReceipt)

        val actualScaffold = service.deploy(request)

        assertThat(actualScaffold).isNotNull
        assertThat(actualScaffold.address).isEqualTo(ADDRESS_VALUE)
    }

    @Test(expected = DeployException::class)
    fun deployWithDeployException() {
        val scaffoldPropertyDto = ScaffoldPropertyDto("name", PropertyType.STRING, "value")
        val request = DeployScaffoldRequest(OPEN_KEY_VALUE, "1", "description", "1", Currency.USD, "1", listOf(scaffoldPropertyDto))

        mockDeploy()
        given(transaction.hasError()).willReturn(true)
        given(transaction.error).willReturn(error)
        given(error.message).willReturn("error")

        service.deploy(request)
    }

    @Test
    fun save() {
        val openKey = OpenKey(user.apply { id = ID }, OPEN_KEY_VALUE).apply { id = ID }
        val scaffold = getScaffold()
        val scaffoldPropertyDto = ScaffoldPropertyDto("name", PropertyType.STRING, "value")
        val scaffoldProperty = ScaffoldProperty.of(scaffold, scaffoldPropertyDto)
        val request = SaveScaffoldRequest(ADDRESS_VALUE, "abi", OPEN_KEY_VALUE, "developerAddress",
                "description", "1", Currency.USD, "1").apply { properties = listOf(scaffoldPropertyDto) }
        val expectedScaffold = getScaffold().apply { property.add(scaffoldProperty) }

        given(openKeyService.get(OPEN_KEY_VALUE)).willReturn(openKey)
        given(repository.save(any(Scaffold::class.java))).will { invocation -> invocation.arguments[0] }
        given(propertyRepository.save(any(ScaffoldProperty::class.java))).will { invocation -> invocation.arguments[0] }

        val actualScaffold = service.save(request)

        assertThat(actualScaffold.abi).isEqualTo(expectedScaffold.abi)
        assertThat(actualScaffold.openKey).isEqualTo(expectedScaffold.openKey)
        assertThat(actualScaffold.property[0].name).isEqualTo(expectedScaffold.property[0].name)
    }

    @Test
    fun setWebHook() {
        val webHookValue = "webHook"
        val expectedScaffold = getScaffold()
        expectedScaffold.webHook = webHookValue
        val request = SetWebHookRequest(webHookValue)

        given(repository.findByAddressAndOpenKeyUser(ADDRESS_VALUE, user)).willReturn(expectedScaffold)

        val actualScaffold = service.setWebHook(ADDRESS_VALUE, request, user)

        Assertions.assertThat(actualScaffold).isEqualTo(expectedScaffold)
    }

    @Test
    fun getScaffoldSummary() {
        val expectedScaffold = getScaffold()

        mockGetCall()
        given(repository.findByAddressAndOpenKeyUser(ADDRESS_VALUE, user)).willReturn(expectedScaffold)

        val actualScaffold = service.getScaffoldSummary(ADDRESS_VALUE, user)

        Assertions.assertThat(actualScaffold).isNotNull
    }

    @Test
    fun deactivate() {
        val expectedScaffold = getScaffold()

        mockGetCall()
        given(repository.findByAddressAndOpenKeyUser(ADDRESS_VALUE, user)).willReturn(expectedScaffold)

        val actualResult = service.deactivate(ADDRESS_VALUE, user)

        assertThat(actualResult).isNotNull
    }

    @Test(expected = FunctionCallException::class)
    fun deactivateWithError() {
        val expectedScaffold = getScaffold()
        val nonce = BigInteger.ZERO

        given(repository.findByAddressAndOpenKeyUser(ADDRESS_VALUE, user)).willReturn(expectedScaffold)
        given(properties.getCredentials()).willReturn(credentials)
        given(credentials.address).willReturn(ADDRESS_VALUE)
        given(web3j.ethGetTransactionCount(ADDRESS_VALUE, DefaultBlockParameterName.LATEST)).willReturn(transactionCountRequest)
        given(transactionCountRequest.send()).willReturn(transactionCount)
        given(transactionCount.transactionCount).willReturn(nonce)
        given(web3j.ethCall(any(Transaction::class.java), any(DefaultBlockParameter::class.java))).willReturn(callRequest)
        given(callRequest.send()).willReturn(call)
        given(call.hasError()).willReturn(true)
        given(call.error).willReturn(error)
        given(error.message).willReturn("error")

        service.deactivate(ADDRESS_VALUE, user)
    }

    @Test
    fun getQuota() {
        val currentCount = 1L
        val expectedQuota = ScaffoldQuotaDto(currentCount, 10L)

        given(repository.countByEnabledIsFalseAndOpenKeyUser(user)).willReturn(currentCount)

        val actualQuota = service.getQuota(user)

        assertThat(actualQuota).isEqualTo(expectedQuota)
    }

    private fun mockDeploy() {
        val scaffoldPropertyDto = ScaffoldPropertyDto("name", PropertyType.STRING, "value")
        val openKey = OpenKey(user)
        val compileScaffoldRequest = CompileScaffoldRequest(openKey.value, listOf(scaffoldPropertyDto))
        val contractMetadata = CompilationResult.ContractMetadata().apply { abi = "abi"; bin = "bin" }

        given(openKeyService.get(OPEN_KEY_VALUE)).willReturn(openKey)
        given(repository.countByEnabledIsFalseAndOpenKeyUser(user)).willReturn(1)
        given(compiler.compile(compileScaffoldRequest.properties)).willReturn(contractMetadata)
        given(web3j.ethGetTransactionCount(ADDRESS_VALUE, DefaultBlockParameterName.LATEST)).willReturn(transactionCountRequest)
        given(transactionCountRequest.send()).willReturn(transactionCount)
        given(properties.getCredentials()).willReturn(credentials)
        given(credentials.address).willReturn(ADDRESS_VALUE)
        given(credentials.ecKeyPair).willReturn(
                ECKeyPair(BigInteger("70434874820561167833413314465717821912032224559303517602656935329389114250303"),
                BigInteger("8552787867577691478484551058702821390038452396941523688407381362313198396896336414469416" +
                        "913650107925323445523542038836977111416625597658430914944261355576")))
        given(web3j.ethGetTransactionCount(ADDRESS_VALUE, DefaultBlockParameterName.LATEST)).willReturn(transactionCountRequest)
        given(transactionCountRequest.send()).willReturn(transactionCount)
        given(transactionCount.transactionCount).willReturn(BigInteger.ZERO)
        given(web3j.ethSendRawTransaction(anyString())).willReturn(transactionRequest)
        given(transactionRequest.send()).willReturn(transaction)
    }

    private fun mockGetCall() {
        given(properties.getCredentials()).willReturn(credentials)
        given(credentials.address).willReturn(ADDRESS_VALUE)
        given(web3j.ethGetTransactionCount(ADDRESS_VALUE, DefaultBlockParameterName.LATEST)).willReturn(transactionCountRequest)
        given(transactionCountRequest.send()).willReturn(transactionCount)
        given(transactionCount.transactionCount).willReturn(BigInteger.ZERO)
        given(web3j.ethCall(any(Transaction::class.java), any(DefaultBlockParameter::class.java))).willReturn(callRequest)
        given(callRequest.send()).willReturn(call)
        given(call.hasError()).willReturn(false)
        given(call.value).willReturn("0xa5643bf2000000000000000000000000000000000000000000000000000000000000006" +
                "0000000000000000000000000000000000000000000000000000000000000000100000000000000000000000" +
                "000000000000000000000000000000000000000a0000000000000000000000000000000000000000000000000000" +
                "00000000000046461766500000000000000000000000000000000000000000000000000000000000000000000000000" +
                "0000000000000000000000000000000000000000000003000000000000000000000000000000000000000000000000000" +
                "0000000000001000000000000000000000000000000000000000000000000000000000000000200000000" +
                "00000000000000000000000000000000000000000000000000000003")
    }

    private fun getScaffold(): Scaffold {
        val openKey = OpenKey(user.apply { id = ID }, OPEN_KEY_VALUE).apply { id = ID }

        return Scaffold(ADDRESS_VALUE, openKey, "abi", ADDRESS_VALUE, "description", "1", 1,
                "1", "webHook", mutableListOf(), true)
    }

}