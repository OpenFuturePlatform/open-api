package io.openfuture.api.controller.widget

import io.openfuture.api.config.ControllerTests
import io.openfuture.api.entity.auth.OpenKey
import io.openfuture.api.entity.auth.User
import io.openfuture.api.entity.scaffold.Currency.USD
import io.openfuture.api.entity.scaffold.Scaffold
import io.openfuture.api.entity.scaffold.ScaffoldVersion.V1
import io.openfuture.api.service.ScaffoldService
import org.junit.Test
import org.mockito.BDDMockito.given
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.*

@WebMvcTest(ScaffoldWidgetController::class)
class ScaffoldWidgetControllerTests : ControllerTests() {

    @MockBean
    private lateinit var service: ScaffoldService


    @Test
    fun getTest() {
        val scaffold = createScaffold()

        given(service.get(scaffold.address)).willReturn(scaffold)

        mvc.perform(get("/widget/scaffolds/" + scaffold.address))

                .andExpect(status().isOk)
                .andExpect(content().json(expectScaffoldJson(scaffold), true))
    }

    private fun expectScaffoldJson(scaffold: Scaffold) = """
        {
                      "address": ${scaffold.address},
                      "abi": ${scaffold.abi},
                      "description": ${scaffold.description},
                      "fiatAmount": "${scaffold.fiatAmount}",
                      "currency": ${scaffold.getCurrency().name},
                      "conversionAmount": "${scaffold.conversionAmount}",
                      "properties": ${Arrays.toString(scaffold.property.toTypedArray())}
                    }
    """.trimIndent()

    private fun createScaffold() = Scaffold("address", OpenKey(User("")), "abi", "developerAddress",
            "description", "2", USD.getId(), "0.00023", V1.getId())

}