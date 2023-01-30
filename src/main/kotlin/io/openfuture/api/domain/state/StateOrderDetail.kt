package io.openfuture.api.domain.state

import java.math.BigDecimal

data class StateOrderDetail(
    var orderKey: String,
    var amount: BigDecimal,
    var totalPaid: BigDecimal = BigDecimal.ZERO,
    var currency: String,
    val blockchains: List<BlockchainWallets>
)

data class BlockchainWallets(
    val address: String,
    val blockchain: String,
    val rate: BigDecimal
)
