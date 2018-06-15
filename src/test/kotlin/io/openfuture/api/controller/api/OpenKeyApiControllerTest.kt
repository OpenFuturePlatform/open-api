package io.openfuture.api.controller.api

import io.openfuture.api.config.ControllerTests
import io.openfuture.api.entity.auth.OpenKey
import org.junit.Test
import org.mockito.BDDMockito.given
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

/**
 * @author Yauheni Efimenko
 */
@WebMvcTest(OpenKeyApiController::class)
class OpenKeyApiControllerTest : ControllerTests() {

    @Test
    fun generateToken() {
        val newOpenKey = OpenKey(openKey.user)

        given(keyService.generate(openKey.user)).willReturn(newOpenKey)

        mvc.perform(post("/api/keys")
                .header(AUTHORIZATION, OPEN_TOKEN_VALUE))

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
        whenOpenTokenIsNotFoundShouldRedirectToIndexPage("/api/keys")
    }

}
