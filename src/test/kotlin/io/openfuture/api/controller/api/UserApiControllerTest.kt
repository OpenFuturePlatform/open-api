package io.openfuture.api.controller.api

import io.openfuture.api.config.ControllerTests
import org.junit.Test
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

/**
 * @author Yauheni Efimenko
 */
@WebMvcTest(UserApiController::class)
class UserApiControllerTest : ControllerTests() {

    @Test
    fun getCurrent() {
        mvc.perform(get("/api/users/current")
                .header(AUTHORIZATION, OPEN_TOKEN_VALUE))

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
        whenOpenTokenIsNotFoundShouldRedirectToIndexPage("/api/users/current")
    }

}
