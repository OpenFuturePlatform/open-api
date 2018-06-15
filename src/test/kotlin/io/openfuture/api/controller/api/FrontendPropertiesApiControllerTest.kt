package io.openfuture.api.controller.api

import io.openfuture.api.config.ControllerTests
import io.openfuture.api.config.propety.EthereumProperties
import org.junit.Test
import org.mockito.BDDMockito.given
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
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

        given(ethereumProperties.infura).willReturn(infuraUrl)

        mvc.perform(MockMvcRequestBuilders.get("/api/properties")
                .header(AUTHORIZATION, OPEN_TOKEN_VALUE))

                .andExpect(status().isOk)
                .andExpect(content().json("""
                    {
                        "infura": $infuraUrl
                    }
                    """.trimIndent(), true))
    }

    @Test
    fun getWhenOpenTokenIsNotFoundShouldRedirectToIndexPage() {
        whenOpenTokenIsNotFoundShouldRedirectToIndexPage("/api/properties")
    }

}
