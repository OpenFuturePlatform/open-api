package io.openfuture.api.controller.widget

import io.openfuture.api.config.ControllerTests
import io.openfuture.api.domain.state.StateWalletTransactionDetail
import io.openfuture.api.entity.scaffold.EthereumScaffold
import io.openfuture.api.service.ApplicationWalletService
import org.junit.Test
import org.mockito.BDDMockito
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.math.BigDecimal
import java.util.*

@WebMvcTest(TransactionWidgetController::class)
class TransactionWidgetControllerTest: ControllerTests() {

    @MockBean
    private lateinit var service : ApplicationWalletService

    @Test
    fun getTest() {
        val detail = createDummy()

        BDDMockito.given(service.getAddressTransactionsByAddress("address")).willReturn(detail)

       /* mvc.perform(get("/widget/transactions/address" ))

            .andExpect(status().isOk)
            .andExpect(content().json(expectDataJson(detail), true))*/
    }

    private fun expectDataJson(stateWalletTransactionDetail: StateWalletTransactionDetail) = """
        {
                      "orderKey": ${stateWalletTransactionDetail.orderKey},
                      "amount": ${stateWalletTransactionDetail.amount},
                      "totalPaid": ${stateWalletTransactionDetail.totalPaid},
                      "rate": "${stateWalletTransactionDetail.rate}",
                      "transactions": ${stateWalletTransactionDetail.transactions}
                    }
    """.trimIndent()

    private fun createDummy() = StateWalletTransactionDetail("wc_order_JTj1BJyvtl9j3", BigDecimal.ZERO, BigDecimal.ZERO,BigDecimal.ZERO, listOf())
}