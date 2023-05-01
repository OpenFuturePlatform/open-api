package io.openfuture.api.controller.widget

import io.openfuture.api.config.ControllerTests
import io.openfuture.api.entity.auth.User
import io.openfuture.api.entity.scaffold.Currency.USD
import io.openfuture.api.entity.scaffold.EthereumScaffold
import io.openfuture.api.entity.scaffold.ScaffoldVersion.V1
import io.openfuture.api.service.EthereumScaffoldService
import org.junit.Test
import org.mockito.BDDMockito.given
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.DisabledIf
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.*

@WebMvcTest(ScaffoldWidgetController::class)
class ScaffoldWidgetControllerTests : ControllerTests() {

    @MockBean
    private lateinit var service: EthereumScaffoldService


    @DisabledIf
    @Test
    fun getTest() {
        val scaffold = createScaffold()

        given(service.get(scaffold.address)).willReturn(scaffold)

        mvc.perform(get("/widget/scaffolds/" + scaffold.address))

                .andExpect(status().isOk)
                .andExpect(content().json(expectScaffoldJson(scaffold), true))
    }

    private fun expectScaffoldJson(ethereumScaffold: EthereumScaffold) = """
        {
                      "address": ${ethereumScaffold.address},
                      "abi": ${ethereumScaffold.abi},
                      "description": ${ethereumScaffold.description},
                      "fiatAmount": "${ethereumScaffold.fiatAmount}",
                      "currency": ${ethereumScaffold.getCurrency().name},
                      "conversionAmount": "${ethereumScaffold.conversionAmount}",
                      "properties": ${Arrays.toString(ethereumScaffold.property.toTypedArray())}
                    }
    """.trimIndent()

    private fun createScaffold() = EthereumScaffold("address", "abi", "2", USD.getId(), "0.00023", mutableListOf(), V1.getId(), "developerAddress", "description")

}