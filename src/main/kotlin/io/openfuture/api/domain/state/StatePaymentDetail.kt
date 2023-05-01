package io.openfuture.api.domain.state

import java.math.BigDecimal

data class StatePaymentDetail(
    var orderKey: String,
    var amount: BigDecimal,
    var totalPaid: BigDecimal = BigDecimal.ZERO,
    var currency: String,
    val blockchains: List<BlockchainWallet>
)

data class BlockchainWallet(
    val address: String,
    val blockchain: String,
    val rate: BigDecimal,
    val encrypted: String? = ""
)
