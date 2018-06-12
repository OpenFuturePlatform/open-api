package io.openfuture.api.service

import io.openfuture.api.component.ScaffoldCompiler
import io.openfuture.api.config.propety.EthereumProperties
import io.openfuture.api.domain.scaffold.CompileScaffoldRequest
import io.openfuture.api.domain.scaffold.CompiledScaffoldDto
import io.openfuture.api.domain.scaffold.ScaffoldQuotaDto
import io.openfuture.api.domain.scaffold.SetWebHookRequest
import io.openfuture.api.entity.auth.OpenKey
import io.openfuture.api.entity.auth.User
import io.openfuture.api.entity.scaffold.Scaffold
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
import java.util.*

/**
 * @author Alexey Skadorva
 */
internal class DefaultScaffoldServiceTest : ServiceTest() {

    @Mock private lateinit var pageable: Pageable
    @Mock private lateinit var compiler: ScaffoldCompiler
    @Mock private lateinit var repository: ScaffoldRepository
    @Mock private lateinit var properties: EthereumProperties
    @Mock private lateinit var openKeyService: OpenKeyService
    @Mock private lateinit var propertyRepository: ScaffoldPropertyRepository

    private lateinit var service: ScaffoldService


    @Before
    fun setUp() {
        service = DefaultScaffoldService(repository, propertyRepository, compiler, properties, openKeyService)
    }

    @Test
    fun getAll() {
        val user = User(GOOGLE_ID)
        val expectedScaffoldPages = PageImpl(Collections.singletonList(getScaffold()), pageable, 1)

        given(repository.findAllByOpenKeyUser(user, pageable)).willReturn(expectedScaffoldPages)

        val actualScaffoldPages = service.getAll(user, pageable)

        Assertions.assertThat(actualScaffoldPages).isEqualTo(expectedScaffoldPages)
    }

    @Test
    fun get() {
        val user = User(GOOGLE_ID)
        val address = "address"
        val expectedScaffold = getScaffold()
        given(repository.findByAddressAndOpenKeyUser(address, user)).willReturn(expectedScaffold)

        val actualScaffold = service.get(address, user)

        Assertions.assertThat(actualScaffold).isEqualTo(expectedScaffold)
    }

    @Test(expected = NotFoundException::class)
    fun getWithNotFoundException() {
        val user = User(GOOGLE_ID)
        val address = "address"
        given(repository.findByAddressAndOpenKeyUser(address, user)).willReturn(null)

        service.get(address, user)
    }


    @Test
    fun compile() {
        val user = User(GOOGLE_ID)
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
        val user = User(GOOGLE_ID)
        val openKey = OpenKey(user)
        val request = CompileScaffoldRequest(OPEN_KEY_VALUE)
        given(openKeyService.get(OPEN_KEY_VALUE)).willReturn(openKey)
        given(repository.countByEnabledIsFalseAndOpenKeyUser(user)).willReturn(11)

        service.compile(request)
    }

    @Test
    fun deploy() {

    }

//    @Test
//    fun save() {
//        val user = User(GOOGLE_ID)
//        val openKey = OpenKey(user, OPEN_KEY_VALUE)
//        val scaffold = getScaffold()
//        val scaffoldPropertyDto = ScaffoldPropertyDto("name", PropertyType.STRING, "value")
//        val scaffoldProperty = ScaffoldProperty.of(scaffold, scaffoldPropertyDto)
//        val request = SaveScaffoldRequest("address", "abi", OPEN_KEY_VALUE, "developerAddress", "description", "" , Currency.USD, "").apply{ properties = listOf(scaffoldPropertyDto) }
//        val expectedScaffold = Scaffold("address", openKey, "abi", "developerAddress", "description", "fiatAmount", 1,
//                "conversionAmount", mutableListOf(scaffoldProperty), true, "webHook").apply { id = ID; }
//
//        given(openKeyService.get(OPEN_KEY_VALUE)).willReturn(openKey)
//        given(repository.save(Scaffold.of(request, openKey))).willReturn(scaffold)
//
//        given(propertyRepository.save(ScaffoldProperty.of(scaffold, scaffoldPropertyDto))).willReturn(scaffoldProperty.apply { id = ID })
//
//        val actualScaffold = service.save(request)
//
//        assertThat(actualScaffold).isEqualTo(expectedScaffold)
//    }

    @Test
    fun setWebHook() {
        val webHookValue = "webHook"
        val user = User(GOOGLE_ID)
        val address = "address"
        val expectedScaffold = getScaffold()
        expectedScaffold.webHook = webHookValue
        val request = SetWebHookRequest(webHookValue)
        given(repository.findByAddressAndOpenKeyUser(address, user)).willReturn(expectedScaffold)

        val actualScaffold = service.setWebHook(address, request, user)

        Assertions.assertThat(actualScaffold).isEqualTo(expectedScaffold)
    }
    @Test
    fun getScaffoldSummary() {
    }

    @Test
    fun deactivate() {
    }

    @Test
    fun getQuota() {
        val currentCount = 1L
        val user = User(GOOGLE_ID)
        val expectedQuota = ScaffoldQuotaDto(currentCount, 10L)

        given(repository.countByEnabledIsFalseAndOpenKeyUser(user)).willReturn(currentCount)

        val actualQuota = service.getQuota(user)

        assertThat(actualQuota).isEqualTo(expectedQuota)
    }

    private fun getScaffold(): Scaffold {
        val openKey = OpenKey(User(GOOGLE_ID))

        return Scaffold("address", openKey, "abi", "developerAddress", "description", "fiatAmount", 1,
                "conversionAmount", Collections.emptyList(), true, "webHook")
    }

}