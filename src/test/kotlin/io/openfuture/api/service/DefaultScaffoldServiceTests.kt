package io.openfuture.api.service

import io.openfuture.api.component.ScaffoldCompiler
import io.openfuture.api.component.TransactionHandler
import io.openfuture.api.config.UnitTest
import io.openfuture.api.config.any
import io.openfuture.api.config.anyString
import io.openfuture.api.config.propety.EthereumProperties
import io.openfuture.api.domain.scaffold.*
import io.openfuture.api.entity.auth.OpenKey
import io.openfuture.api.entity.auth.User
import io.openfuture.api.entity.scaffold.Currency
import io.openfuture.api.entity.scaffold.PropertyType.STRING
import io.openfuture.api.entity.scaffold.Scaffold
import io.openfuture.api.entity.scaffold.ScaffoldProperty
import io.openfuture.api.exception.DeployException
import io.openfuture.api.exception.FunctionCallException
import io.openfuture.api.exception.NotFoundException
import io.openfuture.api.repository.ScaffoldPropertyRepository
import io.openfuture.api.repository.ScaffoldRepository
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

    private val user = User("104113085667282103363")
    private val openKeyValue = "op_pk_9de7cbb4-857c-49e9-87d2-fc91428c4c12"
    private val addressValue = "0xba37163625b3f2e96112562858c12b75963af138"


    @Before
    fun setUp() {
        service = DefaultScaffoldService(web3j, repository, propertyRepository, compiler, properties, openKeyService, transactionHandler)
    }

    @Test
    fun getAll() {
        val expectedScaffoldPages = PageImpl(Collections.singletonList(createScaffold()), pageable, 1)

        given(repository.findAllByOpenKeyUser(user, pageable)).willReturn(expectedScaffoldPages)

        val actualScaffoldPages = service.getAll(user, pageable)

        assertThat(actualScaffoldPages).isEqualTo(expectedScaffoldPages)
    }

    @Test
    fun get() {
        val expectedScaffold = createScaffold()
        given(repository.findByAddressAndOpenKeyUser(addressValue, user)).willReturn(expectedScaffold)

        val actualScaffold = service.get(addressValue, user)

        assertThat(actualScaffold).isEqualTo(expectedScaffold)
    }

    @Test(expected = NotFoundException::class)
    fun getWithNotFoundException() {
        given(repository.findByAddressAndOpenKeyUser(addressValue, user)).willReturn(null)

        service.get(addressValue, user)
    }


    @Test
    fun compile() {
        val openKey = OpenKey(user)
        val request = CompileScaffoldRequest(openKeyValue)
        val contractMetadata = CompilationResult.ContractMetadata().apply { abi = "abi"; bin = "bin" }
        val expectedContractMetadata = CompiledScaffoldDto(contractMetadata)

        given(openKeyService.get(openKeyValue)).willReturn(openKey)
        given(repository.countByEnabledIsFalseAndOpenKeyUser(user)).willReturn(1)
        given(compiler.compile(request.properties)).willReturn(contractMetadata)

        val actualContractMetadata = service.compile(request)

        assertThat(actualContractMetadata).isEqualTo(expectedContractMetadata)
    }

    @Test(expected = IllegalStateException::class)
    fun compileWithExceedingNumberOfAvailableDisabledScaffolds() {
        val openKey = OpenKey(user)
        val request = CompileScaffoldRequest(openKeyValue)

        given(openKeyService.get(openKeyValue)).willReturn(openKey)
        given(repository.countByEnabledIsFalseAndOpenKeyUser(user)).willReturn(11)

        service.compile(request)
    }

    @Test
    fun deploy() {
        val expectedScaffold = createScaffold()
        val request = DeployScaffoldRequest(openKeyValue, addressValue, "description", "1", Currency.USD, "1",
                null, listOf(createScaffoldPropertyDto()))
        val optionalTransactionReceipt = Optional.of(TransactionReceipt().apply { contractAddress = addressValue })
        val contractMetadata = CompilationResult.ContractMetadata().apply { abi = "abi"; bin = "bin" }

        mockDeploy()

        given(repository.save(any(Scaffold::class.java))).will {invocation -> invocation.arguments[0] }
        given(propertyRepository.save(any(ScaffoldProperty::class.java))).will {invocation -> invocation.arguments[0] }
        given(openKeyService.get(openKeyValue)).willReturn(expectedScaffold.openKey)
        given(compiler.compile(request.properties)).willReturn(contractMetadata)
        given(transaction.hasError()).willReturn(false)
        given(web3j.ethGetTransactionReceipt(transaction.transactionHash)).willReturn(transactionReceiptRequest)
        given(transactionReceiptRequest.send()).willReturn(transactionReceipt)
        given(transactionReceipt.transactionReceipt).willReturn(optionalTransactionReceipt)

        val actualScaffold = service.deploy(request)

        assertThat(actualScaffold).isNotNull
        assertThat(actualScaffold.address).isEqualTo(addressValue)
    }

    @Test(expected = DeployException::class)
    fun deployWithDeployException() {
        val request = DeployScaffoldRequest(openKeyValue, "1", "description", "1", Currency.USD, "1",
                null, listOf(createScaffoldPropertyDto()))

        mockDeploy()
        given(transaction.hasError()).willReturn(true)
        given(transaction.error).willReturn(error)
        given(error.message).willReturn("error")

        service.deploy(request)
    }

    @Test
    fun save() {
        val openKey = OpenKey(user.apply { id = 1L }, null, openKeyValue).apply { id = 1L }
        val scaffold = createScaffold()
        val scaffoldPropertyDto = createScaffoldPropertyDto()
        val scaffoldProperty = ScaffoldProperty.of(scaffold, scaffoldPropertyDto)
        val request = SaveScaffoldRequest(addressValue, "abi", openKeyValue, "developerAddress",
                "description", "1", Currency.USD, "1").apply { properties = listOf(scaffoldPropertyDto) }
        val expectedScaffold = createScaffold().apply { property.add(scaffoldProperty) }

        given(openKeyService.get(openKeyValue)).willReturn(openKey)
        given(repository.save(any(Scaffold::class.java))).will { invocation -> invocation.arguments[0] }
        given(propertyRepository.save(any(ScaffoldProperty::class.java))).will { invocation -> invocation.arguments[0] }

        val actualScaffold = service.save(request)

        assertThat(actualScaffold.abi).isEqualTo(expectedScaffold.abi)
        assertThat(actualScaffold.openKey).isEqualTo(expectedScaffold.openKey)
        assertThat(actualScaffold.property[0].name).isEqualTo(expectedScaffold.property[0].name)
    }

    @Test
    fun update() {
        val description = "description"
        val scaffold = createScaffold()
        val request = UpdateScaffoldRequest(description)

        given(repository.findByAddressAndOpenKeyUser(addressValue, user)).willReturn(scaffold)
        given(repository.save(any(Scaffold::class.java))).will { invocation -> invocation.arguments[0] }

        val actualScaffold = service.update(addressValue, user, request)

        assertThat(actualScaffold.address).isEqualTo(scaffold.address)
        assertThat(actualScaffold.description).isEqualTo(description)
    }

    @Test
    fun setWebHook() {
        val webHookValue = "webHook"
        val expectedScaffold = createScaffold()
        expectedScaffold.webHook = webHookValue
        val request = SetWebHookRequest(webHookValue)

        given(repository.findByAddressAndOpenKeyUser(addressValue, user)).willReturn(expectedScaffold)
        given(repository.save(expectedScaffold)).will { invocation -> invocation.arguments[0] }

        val actualScaffold = service.setWebHook(addressValue, request, user)

        assertThat(actualScaffold).isEqualTo(expectedScaffold)
    }

    @Test
    fun getScaffoldSummary() {
        val expectedScaffold = createScaffold()

        mockGetCall()
        given(repository.findByAddressAndOpenKeyUser(addressValue, user)).willReturn(expectedScaffold)

        val actualScaffold = service.getScaffoldSummary(addressValue, user)

        assertThat(actualScaffold).isNotNull
    }

    @Test
    fun deactivate() {
        val expectedScaffold = createScaffold()

        mockGetCall()
        given(repository.findByAddressAndOpenKeyUser(addressValue, user)).willReturn(expectedScaffold)

        val actualResult = service.deactivate(addressValue, user)

        assertThat(actualResult).isNotNull
    }

    @Test(expected = FunctionCallException::class)
    fun deactivateWithError() {
        val expectedScaffold = createScaffold()
        val nonce = BigInteger.ZERO

        given(repository.findByAddressAndOpenKeyUser(addressValue, user)).willReturn(expectedScaffold)
        given(properties.getCredentials()).willReturn(credentials)
        given(credentials.address).willReturn(addressValue)
        given(web3j.ethGetTransactionCount(addressValue, DefaultBlockParameterName.LATEST)).willReturn(transactionCountRequest)
        given(transactionCountRequest.send()).willReturn(transactionCount)
        given(transactionCount.transactionCount).willReturn(nonce)
        given(web3j.ethCall(any(Transaction::class.java), any(DefaultBlockParameter::class.java))).willReturn(callRequest)
        given(callRequest.send()).willReturn(call)
        given(call.hasError()).willReturn(true)
        given(call.error).willReturn(error)
        given(error.message).willReturn("error")

        service.deactivate(addressValue, user)
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
        val openKey = OpenKey(user)
        val compileScaffoldRequest = CompileScaffoldRequest(openKey.value, listOf(createScaffoldPropertyDto()))
        val contractMetadata = CompilationResult.ContractMetadata().apply { abi = "abi"; bin = "bin" }

        given(openKeyService.get(openKeyValue)).willReturn(openKey)
        given(repository.countByEnabledIsFalseAndOpenKeyUser(user)).willReturn(1)
        given(compiler.compile(compileScaffoldRequest.properties)).willReturn(contractMetadata)
        given(web3j.ethGetTransactionCount(addressValue, DefaultBlockParameterName.LATEST)).willReturn(transactionCountRequest)
        given(transactionCountRequest.send()).willReturn(transactionCount)
        given(properties.getCredentials()).willReturn(credentials)
        given(credentials.address).willReturn(addressValue)
        given(credentials.ecKeyPair).willReturn(
                ECKeyPair(BigInteger("70434874820561167833413314465717821912032224559303517602656935329389114250303"),
                BigInteger("8552787867577691478484551058702821390038452396941523688407381362313198396896336414469416" +
                        "913650107925323445523542038836977111416625597658430914944261355576")))
        given(web3j.ethGetTransactionCount(addressValue, DefaultBlockParameterName.LATEST)).willReturn(transactionCountRequest)
        given(transactionCountRequest.send()).willReturn(transactionCount)
        given(transactionCount.transactionCount).willReturn(BigInteger.ZERO)
        given(web3j.ethSendRawTransaction(anyString())).willReturn(transactionRequest)
        given(transactionRequest.send()).willReturn(transaction)
    }

    @Test
    fun addShareHolder() {
        val scaffold = createScaffold()
        val request = AddShareHolderRequest(addressValue, 5)

        mockGetCall()
        given(repository.findByAddressAndOpenKeyUser(addressValue, user)).willReturn(scaffold)

        service.addShareHolder(addressValue, user, request)
    }

    @Test
    fun updateShareHolder() {
        val scaffold = createScaffold()
        val request = UpdateShareHolderRequest(addressValue, 10)

        mockGetCall()
        given(repository.findByAddressAndOpenKeyUser(addressValue, user)).willReturn(scaffold)

        service.updateShareHolder(addressValue, user, request)
    }

    @Test
    fun removeShareHolder() {
        val scaffold = createScaffold()
        val request = RemoveShareHolderRequest(addressValue)

        mockGetCall()
        given(repository.findByAddressAndOpenKeyUser(addressValue, user)).willReturn(scaffold)

        service.removeShareHolder(addressValue, user, request)
    }

    private fun mockGetCall() {
        given(properties.getCredentials()).willReturn(credentials)
        given(credentials.address).willReturn(addressValue)
        given(web3j.ethGetTransactionCount(addressValue, DefaultBlockParameterName.LATEST)).willReturn(transactionCountRequest)
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

    private fun createScaffold(): Scaffold {
        val openKey = OpenKey(user.apply { id = 1L }, null, openKeyValue).apply { id = 1L }

        return Scaffold(addressValue, openKey, "abi", addressValue, "description", "1", 1,
                "1", "webHook", mutableListOf(), true)
    }

    private fun createScaffoldPropertyDto(): ScaffoldPropertyDto = ScaffoldPropertyDto("name", STRING, "value")

}