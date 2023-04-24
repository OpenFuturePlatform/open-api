package io.openfuture.api.controller.widget

import io.openfuture.api.config.ControllerTests
import io.openfuture.api.domain.state.OrderTransactionDetail
import io.openfuture.api.service.ApplicationWalletService
import org.junit.Test
import org.mockito.BDDMockito
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import java.math.BigDecimal

@WebMvcTest(TransactionWidgetController::class)
class TransactionWidgetControllerTest: ControllerTests() {

    @MockBean
    private lateinit var service : ApplicationWalletService

    @Test
    fun getTest() {
        val detail = createDummy()

        BDDMockito.given(service.getOrderTransactionsByAddress("address")).willReturn(detail)

       /* mvc.perform(get("/widget/transactions/address" ))

            .andExpect(status().isOk)
            .andExpect(content().json(expectDataJson(detail), true))*/
    }

    private fun expectDataJson(orderTransactionDetail: OrderTransactionDetail) = """
        {
                      "orderKey": ${orderTransactionDetail.orderKey},
                      "amount": ${orderTransactionDetail.amount},
                      "totalPaid": ${orderTransactionDetail.totalPaid},
                      "rate": "${orderTransactionDetail.rate}",
                      "transactions": ${orderTransactionDetail.transactions}
                    }
    """.trimIndent()

    private fun createDummy() = OrderTransactionDetail("wc_order_JTj1BJyvtl9j3", BigDecimal.ZERO, BigDecimal.ZERO,BigDecimal.ZERO, listOf())
}