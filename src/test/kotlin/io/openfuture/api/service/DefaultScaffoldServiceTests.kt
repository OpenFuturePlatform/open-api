package io.openfuture.api.service

import io.openfuture.api.component.scaffold.processor.ScaffoldProcessor
import io.openfuture.api.config.UnitTest
import io.openfuture.api.config.any
import io.openfuture.api.config.propety.ScaffoldProperties
import io.openfuture.api.domain.holder.AddShareHolderRequest
import io.openfuture.api.domain.holder.UpdateShareHolderRequest
import io.openfuture.api.domain.scaffold.*
import io.openfuture.api.entity.auth.OpenKey
import io.openfuture.api.entity.auth.User
import io.openfuture.api.entity.scaffold.Currency.USD
import io.openfuture.api.entity.scaffold.Scaffold
import io.openfuture.api.entity.scaffold.ScaffoldSummary
import io.openfuture.api.exception.NotFoundException
import io.openfuture.api.repository.ScaffoldPropertyRepository
import io.openfuture.api.repository.ScaffoldRepository
import io.openfuture.api.repository.ScaffoldSummaryRepository
import io.openfuture.api.repository.ShareHolderRepository
import org.assertj.core.api.Assertions.assertThat
import org.ethereum.solidity.compiler.CompilationResult
import org.junit.Before
import org.junit.Test
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.Mockito.never
import org.mockito.Mockito.verify
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import java.math.BigInteger.ONE
import java.math.BigInteger.ZERO
import java.util.*

internal class DefaultScaffoldServiceTests : UnitTest() {

    @Mock private lateinit var processor: ScaffoldProcessor
    @Mock private lateinit var properties: ScaffoldProperties
    @Mock private lateinit var repository: ScaffoldRepository
    @Mock private lateinit var openKeyService: OpenKeyService
    @Mock private lateinit var propertyRepository: ScaffoldPropertyRepository
    @Mock private lateinit var summaryRepository: ScaffoldSummaryRepository
    @Mock private lateinit var shareHolderRepository: ShareHolderRepository

    @Mock
    private lateinit var pageable: Pageable

    private lateinit var service: ScaffoldService


    @Before
    fun setUp() {
        service = DefaultScaffoldService(processor, properties, repository, propertyRepository, summaryRepository,
                shareHolderRepository, openKeyService)
    }

    @Test
    fun getAllTest() {
        val user = createUser()
        val expectedScaffoldPages = PageImpl(Collections.singletonList(createScaffold()), pageable, 1)

        given(repository.findAllByOpenKeyUserOrderByIdDesc(user, pageable)).willReturn(expectedScaffoldPages)

        val actualScaffoldPages = service.getAll(user, pageable)

        assertThat(actualScaffoldPages).isEqualTo(expectedScaffoldPages)
    }

    @Test
    fun getTest() {
        val addressValue = "0xba37163625b3f2e96112562858c12b75963af138"
        val user = createUser()
        val expectedScaffold = createScaffold()

        given(repository.findByAddressAndOpenKeyUser(addressValue, user)).willReturn(expectedScaffold)

        val actualScaffold = service.get(addressValue, user)

        assertThat(actualScaffold).isEqualTo(expectedScaffold)
    }

    @Test(expected = NotFoundException::class)
    fun getWhenScaffoldNotFoundShouldTrowExceptionTest() {
        val addressValue = "0xba37163625b3f2e96112562858c12b75963af138"
        val user = createUser()

        given(repository.findByAddressAndOpenKeyUser(addressValue, user)).willReturn(null)

        service.get(addressValue, user)
    }

    @Test
    fun compileTest() {
        val openKeyValue = "op_pk_9de7cbb4-857c-49e9-87d2-fc91428c4c12"
        val user = createUser()
        val openKey = OpenKey(user, Date(), openKeyValue)
        val request = CompileScaffoldRequest(openKeyValue)
        val expectedContractMetadata = CompiledScaffoldDto(CompilationResult.ContractMetadata()
                .apply { abi = "abi"; bin = "bin" })

        given(openKeyService.get(openKeyValue)).willReturn(openKey)
        given(summaryRepository.countByEnabledIsFalseAndScaffoldOpenKeyUser(user)).willReturn(1)
        given(processor.compile(request)).willReturn(expectedContractMetadata)
        given(properties.allowedDisabledContracts).willReturn(10)

        val actualContractMetadata = service.compile(request)

        assertThat(actualContractMetadata).isEqualTo(expectedContractMetadata)
    }

    @Test(expected = IllegalStateException::class)
    fun compileWhenExceedingNumberOfAvailableDisabledScaffoldsShouldThrowExceptionTest() {
        val openKeyValue = "op_pk_9de7cbb4-857c-49e9-87d2-fc91428c4c12"
        val user = createUser()
        val openKey = OpenKey(user, Date(), openKeyValue)
        val request = CompileScaffoldRequest(openKeyValue)

        given(openKeyService.get(openKeyValue)).willReturn(openKey)
        given(summaryRepository.countByEnabledIsFalseAndScaffoldOpenKeyUser(user)).willReturn(11)
        given(properties.allowedDisabledContracts).willReturn(10)

        service.compile(request)

        verify(processor, never()).compile(request)
    }

    @Test
    fun updateTest() {
        val addressValue = "0xba37163625b3f2e96112562858c12b75963af138"
        val user = createUser()
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
    fun setWebHookTest() {
        val addressValue = "0xba37163625b3f2e96112562858c12b75963af138"
        val user = createUser()
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
    fun getScaffoldSummaryWhenExpiredCachePeriodShouldReturnCachedScaffoldSummaryTest() {
        val addressValue = "0xba37163625b3f2e96112562858c12b75963af138"
        val user = createUser()
        val scaffold = createScaffold()
        val scaffoldSummary = ScaffoldSummary(scaffold, ONE, ONE, false)

        given(repository.findByAddressAndOpenKeyUser(addressValue, user)).willReturn(scaffold)
        given(summaryRepository.findByScaffold(scaffold)).willReturn(scaffoldSummary)
        given(properties.cachePeriodInMinutest).willReturn(10)

        val actualScaffoldSummary = service.getScaffoldSummary(addressValue, user)

        assertThat(actualScaffoldSummary).isEqualTo(scaffoldSummary)
    }

    @Test
    fun getScaffoldSummaryShouldReturnActualSummary() {
        val scaffold = createScaffold()
        val expectedSummary = createSummary(scaffold)

        given(repository.findByAddressAndOpenKeyUser(scaffold.address, scaffold.openKey.user)).willReturn(scaffold)
        given(processor.getScaffoldSummary(scaffold)).willReturn(expectedSummary)
        given(summaryRepository.save(any(ScaffoldSummary::class.java)))
                .will { invocation -> invocation.arguments[0] }
        given(processor.getShareHolders(expectedSummary)).willReturn(listOf())

        val actualSummary = service.getScaffoldSummary(scaffold.address, scaffold.openKey.user, true)

        assertThat(actualSummary).isEqualTo(expectedSummary)
    }

    @Test
    fun deactivateShouldReturnActualSummary() {
        val scaffold = createScaffold()
        val expectedSummary = createSummary(scaffold)

        given(repository.findByAddressAndOpenKeyUser(scaffold.address, scaffold.openKey.user)).willReturn(scaffold)
        given(processor.getScaffoldSummary(scaffold)).willReturn(expectedSummary)
        given(summaryRepository.save(any(ScaffoldSummary::class.java)))
                .will { invocation -> invocation.arguments[0] }
        given(processor.getShareHolders(expectedSummary)).willReturn(listOf())

        val actualSummary = service.deactivate(scaffold.address, scaffold.openKey.user)

        verify(processor).deactivate(scaffold)
        assertThat(actualSummary).isEqualTo(expectedSummary)
    }

    @Test
    fun addShareHolderShouldReturnActualSummary() {
        val scaffold = createScaffold()
        val expectedSummary = createSummary(scaffold)
        val request = createAddShareHolderRequest()

        given(repository.findByAddressAndOpenKeyUser(scaffold.address, scaffold.openKey.user)).willReturn(scaffold)
        given(processor.getScaffoldSummary(scaffold)).willReturn(expectedSummary)
        given(summaryRepository.save(any(ScaffoldSummary::class.java)))
                .will { invocation -> invocation.arguments[0] }
        given(processor.getShareHolders(expectedSummary)).willReturn(listOf())

        val actualSummary = service.addShareHolder(scaffold.address, scaffold.openKey.user, request)

        verify(processor).addShareHolder(scaffold, request.address!!, request.percent!!.toLong())
        assertThat(actualSummary).isEqualTo(expectedSummary)
    }

    @Test
    fun updateShareHolderShouldReturnActualSummary() {
        val scaffold = createScaffold()
        val expectedSummary = createSummary(scaffold)
        val request = createUpdateShareHolderRequest()
        val holderAddress = "0x123123123"

        given(repository.findByAddressAndOpenKeyUser(scaffold.address, scaffold.openKey.user)).willReturn(scaffold)
        given(processor.getScaffoldSummary(scaffold)).willReturn(expectedSummary)
        given(summaryRepository.save(any(ScaffoldSummary::class.java)))
                .will { invocation -> invocation.arguments[0] }
        given(processor.getShareHolders(expectedSummary)).willReturn(listOf())

        val actualSummary = service.updateShareHolder(scaffold.address, scaffold.openKey.user, holderAddress, request)

        verify(processor).updateShareHolder(scaffold, holderAddress, request.percent!!.toLong())
        assertThat(actualSummary).isEqualTo(expectedSummary)
    }

    @Test
    fun removeShareHolderShouldReturnActualSummary() {
        val scaffold = createScaffold()
        val expectedSummary = createSummary(scaffold)
        val holderAddress = "0x123123123"

        given(repository.findByAddressAndOpenKeyUser(scaffold.address, scaffold.openKey.user)).willReturn(scaffold)
        given(processor.getScaffoldSummary(scaffold)).willReturn(expectedSummary)
        given(summaryRepository.save(any(ScaffoldSummary::class.java)))
                .will { invocation -> invocation.arguments[0] }
        given(processor.getShareHolders(expectedSummary)).willReturn(listOf())

        val actualSummary = service.removeShareHolder(scaffold.address, scaffold.openKey.user, holderAddress)

        verify(processor).removeShareHolder(scaffold, holderAddress)
        assertThat(actualSummary).isEqualTo(expectedSummary)
    }

    @Test
    fun saveShouldReturnPersistScaffold() {
        val request = createSaveScaffoldRequest()
        val expectedScaffold = createScaffold(request)
        val summary = createSummary(expectedScaffold)

        given(openKeyService.get(expectedScaffold.openKey.value)).willReturn(expectedScaffold.openKey)
        given(repository.findByAddressAndOpenKeyUser(expectedScaffold.address, expectedScaffold.openKey.user))
                .willReturn(expectedScaffold)
        given(repository.save(any(Scaffold::class.java))).will { invocation -> invocation.arguments[0] }
        given(processor.getScaffoldSummary(expectedScaffold)).willReturn(summary)
        given(summaryRepository.save(any(ScaffoldSummary::class.java)))
                .will { invocation -> invocation.arguments[0] }
        given(processor.getShareHolders(summary)).willReturn(listOf())

        val actualScaffold = service.save(request)

        assertThat(actualScaffold).isEqualToComparingFieldByField(expectedScaffold)
    }

    @Test
    fun deployShouldReturnPersistScaffold() {
        val request = createDeployScaffoldRequest()
        val address = "add"
        val compiledScaffold = createCompiledScaffold()
        val expectedScaffold = createScaffold(createSaveScaffoldRequest(address, compiledScaffold.abi, request))
        val compileRequest = createCompileRequest(expectedScaffold.openKey.value)
        val summary = createSummary(expectedScaffold)

        given(summaryRepository.countByEnabledIsFalseAndScaffoldOpenKeyUser(expectedScaffold.openKey.user)).willReturn(0)
        given(properties.allowedDisabledContracts).willReturn(10)
        given(openKeyService.get(expectedScaffold.openKey.value)).willReturn(expectedScaffold.openKey)
        given(processor.compile(compileRequest)).willReturn(compiledScaffold)
        given(processor.deploy(compiledScaffold.bin, request)).willReturn(address)
        given(repository.findByAddressAndOpenKeyUser(expectedScaffold.address, expectedScaffold.openKey.user))
                .willReturn(expectedScaffold)
        given(repository.save(any(Scaffold::class.java))).will { invocation -> invocation.arguments[0] }
        given(processor.getScaffoldSummary(expectedScaffold)).willReturn(summary)
        given(summaryRepository.save(any(ScaffoldSummary::class.java)))
                .will { invocation -> invocation.arguments[0] }
        given(processor.getShareHolders(summary)).willReturn(listOf())

        val actualScaffold = service.deploy(request)

        assertThat(actualScaffold).isEqualToComparingFieldByField(expectedScaffold)
    }

    @Test
    fun getQuotaTest() {
        val user = createUser()
        val currentCount = 1
        val expectedQuota = ScaffoldQuotaDto(currentCount, 10)

        given(summaryRepository.countByEnabledIsFalseAndScaffoldOpenKeyUser(user)).willReturn(currentCount)
        given(properties.allowedDisabledContracts).willReturn(10)

        val actualQuota = service.getQuota(user)

        assertThat(actualQuota).isEqualTo(expectedQuota)
    }

    private fun createScaffold(): Scaffold = createScaffold(createSaveScaffoldRequest())

    private fun createScaffold(request: SaveScaffoldRequest): Scaffold {
        val addressValue = request.address!!
        val user = createUser()
        val openKey = OpenKey(user.apply { id = 1L }, null, request.openKey!!).apply { id = 1L }

        return Scaffold(addressValue, openKey, request.abi!!, request.developerAddress!!, request.description!!,
                request.fiatAmount!!, request.currency!!.getId(), request.conversionAmount!!, request.version.getId(),
                request.webHook)
    }

    private fun createUser(): User = User("104113085667282103363")

    private fun createSummary(scaffold: Scaffold): ScaffoldSummary = ScaffoldSummary(scaffold, ZERO, ZERO, true)

    private fun createAddShareHolderRequest(): AddShareHolderRequest = AddShareHolderRequest("0x123123123", 1)

    private fun createUpdateShareHolderRequest(): UpdateShareHolderRequest = UpdateShareHolderRequest(1)

    private fun createSaveScaffoldRequest(): SaveScaffoldRequest =
            createSaveScaffoldRequest("add", "abi", createDeployScaffoldRequest())

    private fun createSaveScaffoldRequest(address: String, abi: String, request: DeployScaffoldRequest): SaveScaffoldRequest =
            SaveScaffoldRequest(address, abi, request.openKey, request.developerAddress, request.description,
                    request.fiatAmount, request.currency, request.conversionAmount)

    private fun createDeployScaffoldRequest(): DeployScaffoldRequest = DeployScaffoldRequest("ok_123", "da", "desc",
            "1", USD, "0.1")

    private fun createCompiledScaffold(): CompiledScaffoldDto = CompiledScaffoldDto("abi", "bin")

    private fun createCompileRequest(openKey: String): CompileScaffoldRequest = CompileScaffoldRequest(openKey)

}