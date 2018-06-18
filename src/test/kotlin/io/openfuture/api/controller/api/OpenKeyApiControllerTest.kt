package io.openfuture.api.controller.api

import io.openfuture.api.config.ControllerTests
import io.openfuture.api.entity.auth.OpenKey
import io.openfuture.api.entity.auth.Role
import io.openfuture.api.entity.auth.User
import org.junit.Test
import org.mockito.BDDMockito.given
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

/**
 * @author Yauheni Efimenko
 */
@WebMvcTest(OpenKeyApiController::class)
class OpenKeyApiControllerTest : ControllerTests() {

    @Test
    fun generateToken() {
        val openKey = createOpenKey(setOf(Role("ROLE_DEPLOY")))
        val newOpenKey = OpenKey(openKey.user)

        given(keyService.find(openKey.value)).willReturn(openKey)
        given(keyService.generate(openKey.user)).willReturn(newOpenKey)

        mvc.perform(post("/api/keys")
                .header(AUTHORIZATION, openKey.value))

                .andExpect(status().isOk)
                .andExpect(content().json("""
                    {
                      "value": ${newOpenKey.value},
                      "enabled": ${newOpenKey.enabled},
                      "expiredDate": ${newOpenKey.expiredDate}
                    }
                    """.trimIndent(), true))
    }

    @Test
    fun generateTokenWhenOpenTokenIsNotFoundShouldRedirectToIndexPage() {
        val invalidToken = "not_valid_token"

        given(keyService.find(invalidToken)).willReturn(null)

        mvc.perform(MockMvcRequestBuilders.get("/api/keys")
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
