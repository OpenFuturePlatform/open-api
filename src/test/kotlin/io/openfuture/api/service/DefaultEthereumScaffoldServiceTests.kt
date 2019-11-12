package io.openfuture.api.service

import io.openfuture.api.component.scaffold.processor.ScaffoldProcessor
import io.openfuture.api.component.state.StateApi
import io.openfuture.api.config.UnitTest
import io.openfuture.api.config.any
import io.openfuture.api.config.propety.ScaffoldProperties
import io.openfuture.api.domain.holder.AddEthereumShareHolderRequest
import io.openfuture.api.domain.holder.UpdateEthereumShareHolderRequest
import io.openfuture.api.domain.scaffold.*
import io.openfuture.api.entity.auth.OpenKey
import io.openfuture.api.entity.auth.User
import io.openfuture.api.entity.scaffold.Currency.USD
import io.openfuture.api.entity.scaffold.EthereumScaffold
import io.openfuture.api.entity.scaffold.EthereumScaffoldSummary
import io.openfuture.api.exception.NotFoundException
import io.openfuture.api.repository.EthereumScaffoldPropertyRepository
import io.openfuture.api.repository.EthereumScaffoldRepository
import io.openfuture.api.repository.EthereumScaffoldSummaryRepository
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

internal class DefaultEthereumScaffoldServiceTests : UnitTest() {

    @Mock private lateinit var processor: ScaffoldProcessor
    @Mock private lateinit var properties: ScaffoldProperties
    @Mock private lateinit var repositoryEthereum: EthereumScaffoldRepository
    @Mock private lateinit var openKeyService: OpenKeyService
    @Mock private lateinit var propertyRepository: EthereumScaffoldPropertyRepository
    @Mock private lateinit var ethereumScaffoldSummaryRepository: EthereumScaffoldSummaryRepository
    @Mock private lateinit var shareHolderRepository: ShareHolderRepository
    @Mock private lateinit var stateApi: StateApi

    @Mock
    private lateinit var pageable: Pageable

    private lateinit var service: EthereumScaffoldService


    @Before
    fun setUp() {
        service = DefaultEthereumScaffoldService(processor, properties, repositoryEthereum, propertyRepository, ethereumScaffoldSummaryRepository,
                shareHolderRepository, openKeyService, stateApi)
    }

    @Test
    fun getAllTest() {
        val user = createUser()
        val expectedScaffoldPages = PageImpl(Collections.singletonList(createScaffold()), pageable, 1)

        given(repositoryEthereum.findAllByOpenKeyUserOrderByIdDesc(user, pageable)).willReturn(expectedScaffoldPages)

        val actualScaffoldPages = service.getAll(user, pageable)

        assertThat(actualScaffoldPages).isEqualTo(expectedScaffoldPages)
    }

    @Test
    fun getWithUserTest() {
        val addressValue = "0xba37163625b3f2e96112562858c12b75963af138"
        val user = createUser()
        val expectedScaffold = createScaffold()

        given(repositoryEthereum.findByAddressAndOpenKeyUser(addressValue, user)).willReturn(expectedScaffold)

        val actualScaffold = service.get(addressValue, user)

        assertThat(actualScaffold).isEqualTo(expectedScaffold)
    }

    @Test(expected = NotFoundException::class)
    fun getWithUserWhenScaffoldNotFoundShouldTrowExceptionTest() {
        val addressValue = "0xba37163625b3f2e96112562858c12b75963af138"
        val user = createUser()

        given(repositoryEthereum.findByAddressAndOpenKeyUser(addressValue, user)).willReturn(null)

        service.get(addressValue, user)
    }

    @Test
    fun getTest() {
        val addressValue = "0xba37163625b3f2e96112562858c12b75963af138"
        val expectedScaffold = createScaffold()

        given(repositoryEthereum.findByAddress(addressValue)).willReturn(expectedScaffold)

        val actualScaffold = service.get(addressValue)

        assertThat(actualScaffold).isEqualTo(expectedScaffold)
    }

    @Test(expected = NotFoundException::class)
    fun getWhenScaffoldNotFoundShouldTrowExceptionTest() {
        val addressValue = "0xba37163625b3f2e96112562858c12b75963af138"

        service.get(addressValue)
    }

    @Test
    fun compileTest() {
        val openKeyValue = "op_pk_9de7cbb4-857c-49e9-87d2-fc91428c4c12"
        val user = createUser()
        val openKey = OpenKey(user, Date(), openKeyValue)
        val request = CompileEthereumScaffoldRequest(openKeyValue)
        val expectedContractMetadata = CompiledScaffoldDto(CompilationResult.ContractMetadata()
                .apply { abi = "abi"; bin = "bin" })

        given(openKeyService.get(openKeyValue)).willReturn(openKey)
        given(ethereumScaffoldSummaryRepository.countByEnabledIsFalseAndEthereumScaffoldOpenKeyUser(user)).willReturn(1)
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
        val request = CompileEthereumScaffoldRequest(openKeyValue)

        given(openKeyService.get(openKeyValue)).willReturn(openKey)
        given(ethereumScaffoldSummaryRepository.countByEnabledIsFalseAndEthereumScaffoldOpenKeyUser(user)).willReturn(11)
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
        val request = UpdateEthereumScaffoldRequest(description)

        given(repositoryEthereum.findByAddressAndOpenKeyUser(addressValue, user)).willReturn(scaffold)
        given(repositoryEthereum.save(any(EthereumScaffold::class.java))).will { invocation -> invocation.arguments[0] }

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

        given(repositoryEthereum.findByAddressAndOpenKeyUser(addressValue, user)).willReturn(expectedScaffold)
        given(repositoryEthereum.save(expectedScaffold)).will { invocation -> invocation.arguments[0] }

        val actualScaffold = service.setWebHook(addressValue, request, user)

        assertThat(actualScaffold).isEqualTo(expectedScaffold)
    }

    @Test
    fun getScaffoldSummaryWhenExpiredCachePeriodShouldReturnCachedScaffoldSummaryTest() {
        val addressValue = "0xba37163625b3f2e96112562858c12b75963af138"
        val user = createUser()
        val scaffold = createScaffold()
        val scaffoldSummary = EthereumScaffoldSummary(scaffold, ONE, ONE, false)

        given(repositoryEthereum.findByAddressAndOpenKeyUser(addressValue, user)).willReturn(scaffold)
        given(ethereumScaffoldSummaryRepository.findByEthereumScaffold(scaffold)).willReturn(scaffoldSummary)
        given(properties.cachePeriodInMinutest).willReturn(10)

        val actualScaffoldSummary = service.getScaffoldSummary(addressValue, user)

        assertThat(actualScaffoldSummary).isEqualTo(scaffoldSummary)
    }

    @Test
    fun getScaffoldSummaryShouldReturnActualSummary() {
        val scaffold = createScaffold()
        val expectedSummary = createSummary(scaffold)

        given(repositoryEthereum.findByAddressAndOpenKeyUser(scaffold.address, scaffold.openKey.user)).willReturn(scaffold)
        given(processor.getScaffoldSummary(scaffold)).willReturn(expectedSummary)
        given(ethereumScaffoldSummaryRepository.save(any(EthereumScaffoldSummary::class.java)))
                .will { invocation -> invocation.arguments[0] }
        given(processor.getShareHolders(expectedSummary)).willReturn(listOf())

        val actualSummary = service.getScaffoldSummary(scaffold.address, scaffold.openKey.user, true)

        assertThat(actualSummary).isEqualTo(expectedSummary)
    }

    @Test
    fun deactivateShouldReturnActualSummary() {
        val scaffold = createScaffold()
        val expectedSummary = createSummary(scaffold)

        given(repositoryEthereum.findByAddressAndOpenKeyUser(scaffold.address, scaffold.openKey.user)).willReturn(scaffold)
        given(processor.getScaffoldSummary(scaffold)).willReturn(expectedSummary)
        given(ethereumScaffoldSummaryRepository.save(any(EthereumScaffoldSummary::class.java)))
                .will { invocation -> invocation.arguments[0] }
        given(processor.getShareHolders(expectedSummary)).willReturn(listOf())

        val actualSummary = service.deactivate(scaffold.address, scaffold.openKey.user)

        verify(processor).deactivate(scaffold)
        assertThat(actualSummary).isEqualTo(expectedSummary)
    }

    @Test
    fun activateShouldReturnActualSummary() {
        val scaffold = createScaffold()
        val expectedSummary = createSummary(scaffold)

        given(repositoryEthereum.findByAddressAndOpenKeyUser(scaffold.address, scaffold.openKey.user)).willReturn(scaffold)
        given(processor.getScaffoldSummary(scaffold)).willReturn(expectedSummary)
        given(ethereumScaffoldSummaryRepository.save(any(EthereumScaffoldSummary::class.java)))
                .will { invocation -> invocation.arguments[0] }
        given(processor.getShareHolders(expectedSummary)).willReturn(listOf())

        val actualSummary = service.activate(scaffold.address, scaffold.openKey.user)

        verify(processor).activate(scaffold)
        assertThat(actualSummary).isEqualTo(expectedSummary)
    }


    @Test
    fun addShareHolderShouldReturnActualSummary() {
        val scaffold = createScaffold()
        val expectedSummary = createSummary(scaffold)
        val request = createAddShareHolderRequest()

        given(repositoryEthereum.findByAddressAndOpenKeyUser(scaffold.address, scaffold.openKey.user)).willReturn(scaffold)
        given(processor.getScaffoldSummary(scaffold)).willReturn(expectedSummary)
        given(ethereumScaffoldSummaryRepository.save(any(EthereumScaffoldSummary::class.java)))
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
        val holderAddress = "0x8cf1664B09F216538bc9A32B2c26f85a19fd76B5"

        given(repositoryEthereum.findByAddressAndOpenKeyUser(scaffold.address, scaffold.openKey.user)).willReturn(scaffold)
        given(processor.getScaffoldSummary(scaffold)).willReturn(expectedSummary)
        given(ethereumScaffoldSummaryRepository.save(any(EthereumScaffoldSummary::class.java)))
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
        val holderAddress = "0x8cf1664B09F216538bc9A32B2c26f85a19fd76B5"

        given(repositoryEthereum.findByAddressAndOpenKeyUser(scaffold.address, scaffold.openKey.user)).willReturn(scaffold)
        given(processor.getScaffoldSummary(scaffold)).willReturn(expectedSummary)
        given(ethereumScaffoldSummaryRepository.save(any(EthereumScaffoldSummary::class.java)))
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
        given(repositoryEthereum.findByAddressAndOpenKeyUser(expectedScaffold.address, expectedScaffold.openKey.user))
                .willReturn(expectedScaffold)
        given(repositoryEthereum.save(any(EthereumScaffold::class.java))).will { invocation -> invocation.arguments[0] }
        given(processor.getScaffoldSummary(expectedScaffold)).willReturn(summary)
        given(ethereumScaffoldSummaryRepository.save(any(EthereumScaffoldSummary::class.java)))
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

        given(ethereumScaffoldSummaryRepository.countByEnabledIsFalseAndEthereumScaffoldOpenKeyUser(expectedScaffold.openKey.user)).willReturn(0)
        given(properties.allowedDisabledContracts).willReturn(10)
        given(openKeyService.get(expectedScaffold.openKey.value)).willReturn(expectedScaffold.openKey)
        given(processor.compile(compileRequest)).willReturn(compiledScaffold)
        given(processor.deploy(compiledScaffold.bin, request)).willReturn(address)
        given(repositoryEthereum.findByAddressAndOpenKeyUser(expectedScaffold.address, expectedScaffold.openKey.user))
                .willReturn(expectedScaffold)
        given(repositoryEthereum.save(any(EthereumScaffold::class.java))).will { invocation -> invocation.arguments[0] }
        given(processor.getScaffoldSummary(expectedScaffold)).willReturn(summary)
        given(ethereumScaffoldSummaryRepository.save(any(EthereumScaffoldSummary::class.java)))
                .will { invocation -> invocation.arguments[0] }
        given(processor.getShareHolders(summary)).willReturn(listOf())

        val actualScaffold = service.deploy(request)

        assertThat(actualScaffold).isEqualToComparingFieldByField(expectedScaffold)
    }

    @Test
    fun getQuotaTest() {
        val user = createUser()
        val currentCount = 1
        val expectedQuota = EthereumScaffoldQuotaDto(currentCount, 10)

        given(ethereumScaffoldSummaryRepository.countByEnabledIsFalseAndEthereumScaffoldOpenKeyUser(user)).willReturn(currentCount)
        given(properties.allowedDisabledContracts).willReturn(10)

        val actualQuota = service.getQuota(user)

        assertThat(actualQuota).isEqualTo(expectedQuota)
    }

    private fun createScaffold(): EthereumScaffold = createScaffold(createSaveScaffoldRequest())

    private fun createScaffold(request: SaveEthereumScaffoldRequest): EthereumScaffold {
        val addressValue = request.address!!
        val user = createUser()
        val openKey = OpenKey(user.apply { id = 1L }, null, request.openKey!!, 1).apply { id = 1L }

        return EthereumScaffold(addressValue, request.abi!!, request.fiatAmount!!, request.currency!!.getId(), request.conversionAmount!!, mutableListOf(), request.version.getId(), openKey, request.developerAddress!!, request.description!!, request.webHook)
    }

    private fun createUser(): User = User("104113085667282103363")

    private fun createSummary(ethereumScaffold: EthereumScaffold): EthereumScaffoldSummary = EthereumScaffoldSummary(ethereumScaffold, ZERO, ZERO, true)

    private fun createAddShareHolderRequest(): AddEthereumShareHolderRequest = AddEthereumShareHolderRequest("0x8cf1664B09F216538bc9A32B2c26f85a19fd76B5", 1)

    private fun createUpdateShareHolderRequest(): UpdateEthereumShareHolderRequest = UpdateEthereumShareHolderRequest(1)

    private fun createSaveScaffoldRequest(): SaveEthereumScaffoldRequest =
            createSaveScaffoldRequest("add", "abi", createDeployScaffoldRequest())

    private fun createSaveScaffoldRequest(address: String, abi: String, request: DeployEthereumScaffoldRequest): SaveEthereumScaffoldRequest =
            SaveEthereumScaffoldRequest(address, abi, request.openKey, request.developerAddress, request.description,
                    request.fiatAmount, request.currency, request.conversionAmount)

    private fun createDeployScaffoldRequest(): DeployEthereumScaffoldRequest = DeployEthereumScaffoldRequest("ok_123",
            "0x8cf1664B09F216538bc9A32B2c26f85a19fd76B5", "desc", "1", USD, "0.1")

    private fun createCompiledScaffold(): CompiledScaffoldDto = CompiledScaffoldDto("abi", "bin")

    private fun createCompileRequest(openKey: String): CompileEthereumScaffoldRequest = CompileEthereumScaffoldRequest(openKey)

}