package io.openfuture.api.controller.api

import io.openfuture.api.component.web3.Web3Wrapper
import io.openfuture.api.config.ControllerTests
import io.openfuture.api.entity.auth.Role
import org.junit.Test
import org.mockito.BDDMockito.given
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.web3j.spring.autoconfigure.Web3jProperties

@WebMvcTest(FrontendPropertiesApiController::class)
class FrontendPropertiesApiControllerTests : ControllerTests() {

    @MockBean
    private lateinit var web3: Web3Wrapper

    @MockBean
    private lateinit var properties: Web3jProperties


    @Test
    fun getTest() {
        val openKey = createOpenKey(setOf(Role("ROLE_DEPLOY")))
        val version = "version"
        val clientAddress = "clientAddress"

        given(keyService.find(openKey.value)).willReturn(openKey)
        given(web3.getNetVersion()).willReturn(version)
        given(properties.clientAddress).willReturn(clientAddress)

        mvc.perform(get("/api/properties")
                .header(AUTHORIZATION, openKey.value))

                .andExpect(status().isOk)
                .andExpect(content().json("""
                    {
                        "networkAddress": $clientAddress,
                        "networkVersion": $version
                    }
                    """.trimIndent(), true))
    }

    @Test
    fun getWhenOpenTokenIsNotFoundShouldRedirectToIndexPageTest() {
        val invalidToken = "not_valid_token"

        given(keyService.find(invalidToken)).willReturn(null)

        mvc.perform(get("/api/properties")
                .header(AUTHORIZATION, invalidToken))

                .andExpect(status().is3xxRedirection)
                .andExpect(redirectedUrl("http://localhost/"))
    }

}
