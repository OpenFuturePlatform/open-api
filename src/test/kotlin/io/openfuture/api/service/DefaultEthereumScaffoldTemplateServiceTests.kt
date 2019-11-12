package io.openfuture.api.service


import io.openfuture.api.config.UnitTest
import io.openfuture.api.config.any
import io.openfuture.api.domain.scaffold.EthereumScaffoldTemplatePropertyDto
import io.openfuture.api.domain.scaffold.SaveEthereumScaffoldTemplateRequest
import io.openfuture.api.entity.scaffold.EthereumScaffoldTemplate
import io.openfuture.api.entity.scaffold.EthereumScaffoldTemplateProperty
import io.openfuture.api.entity.scaffold.PropertyType.STRING
import io.openfuture.api.exception.NotFoundException
import io.openfuture.api.repository.EthereumScaffoldTemplateRepository
import io.openfuture.api.repository.ScaffoldTemplatePropertyRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.BDDMockito.given
import org.mockito.Mock
import java.util.*

internal class DefaultEthereumScaffoldTemplateServiceTests : UnitTest() {

    @Mock private lateinit var repository: EthereumScaffoldTemplateRepository
    @Mock private lateinit var propertyRepository: ScaffoldTemplatePropertyRepository

    private lateinit var service: EthereumScaffoldTemplateService


    @Before
    fun setUp() {
        service = DefaultEthereumScaffoldTemplateService(repository, propertyRepository)
    }

    @Test
    fun getAllTest() {
        val expectedScaffoldTemplates = listOf(createScaffoldTemplate())

        given(repository.findAllByDeletedIsFalse()).willReturn(expectedScaffoldTemplates)

        val actualScaffoldTemplates = service.getAll()

        assertThat(actualScaffoldTemplates).isEqualTo(expectedScaffoldTemplates)
    }

    @Test
    fun getTest() {
        val scaffoldTemplateId = 1L
        val scaffoldTemplate = createScaffoldTemplate().apply { id = scaffoldTemplateId }

        given(repository.findById(scaffoldTemplateId)).willReturn(Optional.of(scaffoldTemplate))

        val actualScaffoldTemplates = service.get(scaffoldTemplateId)

        assertThat(actualScaffoldTemplates).isEqualTo(scaffoldTemplate)
    }

    @Test(expected = NotFoundException::class)
    fun getWhenScaffoldTemplateNotFoundShouldThrowExceptionTest() {
        val scaffoldTemplateId = 1L

        given(repository.findById(scaffoldTemplateId)).willReturn(Optional.ofNullable(null))

        service.get(scaffoldTemplateId)
    }

    @Test
    fun saveTest() {
        val request = SaveEthereumScaffoldTemplateRequest("request_name")
        val scaffoldTemplatePropertyDto = EthereumScaffoldTemplatePropertyDto("name", STRING, "value")
        request.apply { properties = listOf(scaffoldTemplatePropertyDto) }

        given(repository.save(any(EthereumScaffoldTemplate::class.java))).will { invocation ->
            invocation.arguments[0] as EthereumScaffoldTemplate
        }
        given(propertyRepository.save(any(EthereumScaffoldTemplateProperty::class.java))).will { invocation ->
            invocation.arguments[0] as EthereumScaffoldTemplateProperty
        }

        val actualScaffoldTemplate = service.save(request)

        assertThat(actualScaffoldTemplate.name).isEqualTo(request.name)
        assertThat(actualScaffoldTemplate.property[0].name).isEqualTo(scaffoldTemplatePropertyDto.name)
    }

    @Test
    fun disableTest() {
        val scaffoldTemplateId = 1L
        val scaffoldTemplate = createScaffoldTemplate().apply { id = scaffoldTemplateId }

        given(repository.findById(scaffoldTemplateId)).willReturn(Optional.of(scaffoldTemplate))
        given(repository.save(any(EthereumScaffoldTemplate::class.java))).will { invocation -> invocation.arguments[0] }

        val actualScaffoldTemplate = service.delete(scaffoldTemplateId)

        assertThat(actualScaffoldTemplate.name).isEqualTo(scaffoldTemplate.name)
        assertThat(actualScaffoldTemplate.deleted).isTrue()
    }

    private fun createScaffoldTemplate(): EthereumScaffoldTemplate = EthereumScaffoldTemplate("template_name",
            "0xba37163625b3f2e96112562858c12b75963af138", "description", "1", 1, "1", "webHook", mutableListOf())

}