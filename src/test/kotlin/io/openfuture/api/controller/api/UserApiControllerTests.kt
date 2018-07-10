package io.openfuture.api.controller.api

import io.openfuture.api.config.ControllerTests
import io.openfuture.api.entity.auth.Role
import org.junit.Test
import org.mockito.BDDMockito.given
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(UserApiController::class)
class UserApiControllerTests : ControllerTests() {

    @Test
    fun getCurrentTest() {
        val openKey = createOpenKey(setOf(Role("ROLE_MASTER")))

        given(keyService.find(openKey.value)).willReturn(openKey)

        mvc.perform(get("/api/users/current")
                .header(AUTHORIZATION, openKey.value))

                .andExpect(status().isOk)
                .andExpect(content().json("""{
                    "user": {
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
                    },
                    "token":${openKey.value}
                    }
                    """.trimIndent(), true))
    }

    @Test
    fun getCurrentWhenOpenTokenIsNotFoundShouldRedirectToIndexPageTest() {
        val invalidToken = "not_valid_token"

        given(keyService.find(invalidToken)).willReturn(null)

        mvc.perform(get("/api/users/current")
                .header(AUTHORIZATION, invalidToken))

                .andExpect(status().isUnauthorized)
    }

}
