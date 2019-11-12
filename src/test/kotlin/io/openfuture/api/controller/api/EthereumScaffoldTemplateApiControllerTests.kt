package io.openfuture.api.controller.api

import io.openfuture.api.config.ControllerTests
import io.openfuture.api.domain.scaffold.EthereumScaffoldTemplateDto
import io.openfuture.api.entity.auth.Role
import io.openfuture.api.entity.scaffold.EthereumScaffoldTemplate
import io.openfuture.api.service.EthereumScaffoldTemplateService
import org.junit.Test
import org.mockito.BDDMockito.given
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.*

@WebMvcTest(EthereumScaffoldTemplateApiController::class)
class EthereumScaffoldTemplateApiControllerTests : ControllerTests() {

    @MockBean
    private lateinit var service: EthereumScaffoldTemplateService


    @Test
    fun getAllTest() {
        val openKey = createOpenKey(setOf(Role("ROLE_MASTER")))
        val scaffoldTemplate = EthereumScaffoldTemplate("template", "address", "description", "1", 1, "1", "webHook", Collections.emptyList())

        given(keyService.find(openKey.value)).willReturn(openKey)
        given(service.getAll()).willReturn(listOf(scaffoldTemplate))

        mvc.perform(get("/api/ethereum-scaffolds/templates")
                .header(AUTHORIZATION, openKey.value))

                .andExpect(status().isOk)
                .andExpect(content()
                        .json(objectMapper.writeValueAsString(listOf(EthereumScaffoldTemplateDto(scaffoldTemplate))), true))
    }

}