package io.openfuture.api.domain.widget

import io.openfuture.api.domain.state.StateWalletIdentity
import io.openfuture.api.domain.state.StateWalletTransaction
import io.openfuture.api.domain.transaction.TransactionDto
import io.openfuture.api.util.get7hFromDate
import java.math.BigDecimal
import java.time.LocalDateTime

data class PaymentWidgetResponse(
    val orderDate: String,
    val orderAmount: BigDecimal,
    var paid: BigDecimal = BigDecimal.ZERO,
    val wallets: List<StateWalletTransaction>
) {
    /*constructor() : this(
        orderDate = get7hFromDate(LocalDateTime.now()).toString(),
        orderAmount = BigDecimal.TEN,
        paid = BigDecimal.ONE,
        wallets = listOf(
            StateWalletTransaction("14bsHbTWRBjTDTp97GkFZATT6N9rb385Vz", "BTC", emptyList()),
            StateWalletTransaction("0x1f5c5efa01d9a3942ea1ae7759d42370f636fafb", "ETH", listOf(TransactionDto(
                "0xc75150a2ab782109bd63cc2dc681db6182896204854b400690747f28d1023303",
                "0x508f7b93af8f47cd47f6d3d40a97144cabbb9c9a",
                "0x23cfcd95c69472933d0ccde097592d09f80a6c24",
                BigDecimal(0.0006),
                LocalDateTime.now(),
                11996547,
                "0xaec549ebeee8b9adfeddda401d6262697550ac3d7dfbd6f0df515f847831d647",
                BigDecimal(0.0006)
            )))
        )
    )*/
}
