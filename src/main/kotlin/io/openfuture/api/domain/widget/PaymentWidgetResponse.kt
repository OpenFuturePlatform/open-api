package io.openfuture.api.domain.widget

import io.openfuture.api.domain.key.KeyWalletDto
import java.math.BigDecimal

data class PaymentWidgetResponse(
    val orderDate: String ,
    val orderAmount: BigDecimal ,
    val wallets: List<KeyWalletDto>
)
