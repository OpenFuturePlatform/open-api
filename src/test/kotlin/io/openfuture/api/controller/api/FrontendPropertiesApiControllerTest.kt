package io.openfuture.api.controller.api

import io.openfuture.api.config.ControllerTests
import io.openfuture.api.entity.auth.Role
import org.junit.Test
import org.mockito.BDDMockito.given
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.web3j.protocol.Web3j
import org.web3j.protocol.core.Request
import org.web3j.protocol.core.methods.response.NetVersion
import org.web3j.spring.autoconfigure.Web3jProperties

/**
 * @author Yauheni Efimenko
 */
@WebMvcTest(FrontendPropertiesApiController::class)
class FrontendPropertiesApiControllerTest : ControllerTests() {

    @MockBean
    private lateinit var properties: Web3jProperties

    @MockBean
    private lateinit var web3: Web3j

    @MockBean
    private lateinit var request:Request<String, NetVersion>

    @MockBean
    private lateinit var netVersion: NetVersion


    @Test
    fun get() {
        val openKey = createOpenKey(setOf(Role("ROLE_DEPLOY")))
        val version = "version"
        val clientAddress = "clientAddress"

        given(keyService.find(openKey.value)).willReturn(openKey)
        given(web3.netVersion()).willReturn(request)
        given(request.send()).willReturn(netVersion)
        given(netVersion.netVersion).willReturn(version)
        given(properties.clientAddress).willReturn(clientAddress)

        mvc.perform(MockMvcRequestBuilders.get("/api/properties")
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
    fun getWhenOpenTokenIsNotFoundShouldRedirectToIndexPage() {
        val invalidToken = "not_valid_token"

        given(keyService.find(invalidToken)).willReturn(null)

        mvc.perform(MockMvcRequestBuilders.get("/api/properties")
                .header(AUTHORIZATION, invalidToken))

                .andExpect(status().is3xxRedirection)
                .andExpect(redirectedUrl("http://localhost/"))
    }

}
