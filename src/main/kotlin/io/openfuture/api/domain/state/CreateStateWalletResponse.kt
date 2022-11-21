package io.openfuture.api.domain.state

import java.math.BigDecimal

data class CreateStateWalletResponse(
    val webhook: String,
    val orderKey: String,
    val amount: BigDecimal,
    val wallets: List<WalletCreateResponse>
)

data class WalletCreateResponse(
    val blockchain: String,
    val address: String,
    val rate: BigDecimal
)