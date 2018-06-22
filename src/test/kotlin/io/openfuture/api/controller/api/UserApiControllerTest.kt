package io.openfuture.api.controller.api

import io.openfuture.api.config.ControllerTests
import io.openfuture.api.entity.auth.Role
import org.junit.Test
import org.mockito.BDDMockito.given
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@WebMvcTest(UserApiController::class)
class UserApiControllerTest : ControllerTests() {

    @Test
    fun getCurrentTest() {
        val openKey = createOpenKey(setOf(Role("ROLE_DEPLOY")))

        given(keyService.find(openKey.value)).willReturn(openKey)

        mvc.perform(get("/api/users/current")
                .header(AUTHORIZATION, openKey.value))

                .andExpect(status().isOk)
                .andExpect(content().json("""
                    {
                      "id": ${openKey.user.id},
                      "credits": ${openKey.user.credits},
                      "openKeys": [
                        {
                          "value": ${openKey.value},
                          "enabled": ${openKey.enabled},
                          "expiredDate": ${openKey.expiredDate}
                        }
                      ],
                      "roles": [
                        {
                          "key": ${openKey.user.roles.first().key}
                        }
                      ]
                    }
                    """.trimIndent(), true))
    }

    @Test
    fun getCurrentWhenOpenTokenIsNotFoundShouldRedirectToIndexPage() {
        val invalidToken = "not_valid_token"

        given(keyService.find(invalidToken)).willReturn(null)

        mvc.perform(get("/api/users/current")
                .header(AUTHORIZATION, invalidToken))

                .andExpect(status().is3xxRedirection)
                .andExpect(redirectedUrl("http://localhost/"))
    }

}
