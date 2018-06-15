package io.openfuture.api.service

import io.openfuture.api.ADDRESS_VALUE
import io.openfuture.api.GOOGLE_ID
import io.openfuture.api.ID
import io.openfuture.api.UnitTest
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
internal class DefaultScaffoldTemplateServiceTest : UnitTest() {

    @Mock private lateinit var repository: ScaffoldTemplateRepository
    @Mock private lateinit var propertyRepository: ScaffoldTemplatePropertyRepository

    private lateinit var service: ScaffoldTemplateService


    @Before
    fun setUp() {
        service = DefaultScaffoldTemplateService(repository, propertyRepository)
    }

    @Test
    fun getAll() {
        val user = User(GOOGLE_ID)
        val scaffoldTemplate = ScaffoldTemplate("template_name", user, ADDRESS_VALUE, "description", "1",
                1, "1", "webHook", mutableListOf())
        val expectedScaffoldTemplates = listOf(scaffoldTemplate)

        given(repository.findAllByUserAndDeletedIsFalse(user)).willReturn(expectedScaffoldTemplates)

        val actualScaffoldTemplates = service.getAll(user)

        assertThat(actualScaffoldTemplates).isEqualTo(expectedScaffoldTemplates)
    }

    @Test
    fun save() {
        val user = User(GOOGLE_ID)
        val request = SaveScaffoldTemplateRequest("request_name")
        val scaffoldTemplate = ScaffoldTemplate.of(request, user)
        val scaffoldTemplateProperty = ScaffoldTemplateProperty(scaffoldTemplate, "name", PropertyType.STRING.getId(), "value")
        val scaffoldTemplatePropertyDto = ScaffoldTemplatePropertyDto("name", PropertyType.STRING, "value")
        request.apply { properties = listOf(scaffoldTemplatePropertyDto) }
        val expectedScaffoldTemplate = ScaffoldTemplate.of(request, user).apply { id = ID; property.add(scaffoldTemplateProperty) }

        given(repository.save(any(ScaffoldTemplate::class.java))).willReturn(scaffoldTemplate.apply { id = ID })
        given(propertyRepository.save(any(ScaffoldTemplateProperty::class.java))).willReturn(scaffoldTemplateProperty.apply { id = ID })

        val actualScaffoldTemplate = service.save(request, user)

        assertThat(actualScaffoldTemplate).isEqualTo(expectedScaffoldTemplate)
    }

}