package io.openfuture.api.service


import io.openfuture.api.config.UnitTest
import io.openfuture.api.config.any
import io.openfuture.api.domain.scaffold.SaveScaffoldTemplateRequest
import io.openfuture.api.domain.scaffold.ScaffoldTemplatePropertyDto
import io.openfuture.api.entity.auth.User
import io.openfuture.api.entity.scaffold.PropertyType
import io.openfuture.api.entity.scaffold.ScaffoldTemplate
import io.openfuture.api.entity.scaffold.ScaffoldTemplateProperty
import io.openfuture.api.repository.ScaffoldTemplatePropertyRepository
import io.openfuture.api.repository.ScaffoldTemplateRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.BDDMockito.given
import org.mockito.Mock

/**
 * @author Alexey Skadorva
 */
internal class DefaultScaffoldTemplateServiceTests : UnitTest() {

    @Mock private lateinit var repository: ScaffoldTemplateRepository
    @Mock private lateinit var propertyRepository: ScaffoldTemplatePropertyRepository

    private lateinit var service: ScaffoldTemplateService


    @Before
    fun setUp() {
        service = DefaultScaffoldTemplateService(repository, propertyRepository)
    }

    @Test
    fun getAll() {
        val user = User("104113085667282103363")
        val scaffoldTemplate = ScaffoldTemplate("template_name", user, "0xba37163625b3f2e96112562858c12b75963af138",
                "description", "1", 1, "1", "webHook", mutableListOf())
        val expectedScaffoldTemplates = listOf(scaffoldTemplate)

        given(repository.findAllByUserAndDeletedIsFalse(user)).willReturn(expectedScaffoldTemplates)

        val actualScaffoldTemplates = service.getAll(user)

        assertThat(actualScaffoldTemplates).isEqualTo(expectedScaffoldTemplates)
    }

    @Test
    fun save() {
        val user = User("104113085667282103363").apply { id = 1L }
        val request = SaveScaffoldTemplateRequest("request_name")
        val scaffoldTemplatePropertyDto = ScaffoldTemplatePropertyDto("name", PropertyType.STRING, "value")
        request.apply { properties = listOf(scaffoldTemplatePropertyDto) }

        given(repository.save(any(ScaffoldTemplate::class.java))).will { invocation ->
            invocation.arguments[0] as ScaffoldTemplate
        }
        given(propertyRepository.save(any(ScaffoldTemplateProperty::class.java))).will { invocation ->
            invocation.arguments[0] as ScaffoldTemplateProperty
        }

        val actualScaffoldTemplate = service.save(request, user)

        assertThat(actualScaffoldTemplate.name).isEqualTo(request.name)
        assertThat(actualScaffoldTemplate.user).isEqualTo(user)
        assertThat(actualScaffoldTemplate.property[0].name).isEqualTo(scaffoldTemplatePropertyDto.name)
    }

}