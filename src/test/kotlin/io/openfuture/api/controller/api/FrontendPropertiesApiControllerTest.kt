package io.openfuture.api.controller.api

import io.openfuture.api.config.ControllerTests
import io.openfuture.api.config.propety.EthereumProperties
import io.openfuture.api.entity.auth.OpenKey
import io.openfuture.api.entity.auth.Role
import io.openfuture.api.entity.auth.User
import org.junit.Test
import org.mockito.BDDMockito.given
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

/**
 * @author Yauheni Efimenko
 */
@WebMvcTest(FrontendPropertiesApiController::class)
class FrontendPropertiesApiControllerTest : ControllerTests() {

    @MockBean
    private lateinit var ethereumProperties: EthereumProperties


    @Test
    fun get() {
        val infuraUrl = "infuraUrl"
        val openKey = createOpenKey(setOf(Role("ROLE_DEPLOY")))

        given(keyService.find(openKey.value)).willReturn(openKey)
        given(ethereumProperties.infura).willReturn(infuraUrl)

        mvc.perform(MockMvcRequestBuilders.get("/api/properties")
                .header(AUTHORIZATION, openKey.value))

                .andExpect(status().isOk)
                .andExpect(content().json("""
                    {
                        "infura": $infuraUrl
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
                .andExpect(MockMvcResultMatchers.redirectedUrl("http://localhost/"))
    }

    private fun createOpenKey(roles: Set<Role>): OpenKey {
        val user = User("test", 0, mutableSetOf(), roles)
        val openKey = OpenKey(user, value = "open_token_value")
        openKey.id = 1
        user.id = 1
        user.openKeys.add(openKey)

        return openKey
    }

}
