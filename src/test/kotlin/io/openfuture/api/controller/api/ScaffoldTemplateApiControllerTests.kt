package io.openfuture.api.controller.api

import io.openfuture.api.config.ControllerTests
import io.openfuture.api.domain.scaffold.ScaffoldTemplateDto
import io.openfuture.api.entity.auth.Role
import io.openfuture.api.entity.scaffold.ScaffoldTemplate
import io.openfuture.api.service.ScaffoldTemplateService
import org.junit.Test
import org.mockito.BDDMockito.given
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.*

@WebMvcTest(ScaffoldTemplateApiController::class)
class ScaffoldTemplateApiControllerTests : ControllerTests() {

    @MockBean
    private lateinit var service: ScaffoldTemplateService


    @Test
    fun getAllTest() {
        val openKey = createOpenKey(setOf(Role("ROLE_DEPLOY")))
        val scaffoldTemplate = ScaffoldTemplate("template", "address", "description", "1", 1, "1", "webHook", Collections.emptyList())

        given(keyService.find(openKey.value)).willReturn(openKey)
        given(service.getAll()).willReturn(listOf(scaffoldTemplate))

        mvc.perform(get("/api/scaffolds/templates")
                .header(AUTHORIZATION, openKey.value))

                .andExpect(status().isOk)
                .andExpect(content()
                        .json(objectMapper.writeValueAsString(listOf(ScaffoldTemplateDto(scaffoldTemplate))), true))
    }

}