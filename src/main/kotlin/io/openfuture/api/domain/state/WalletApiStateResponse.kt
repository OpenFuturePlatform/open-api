package io.openfuture.api.domain.state

import java.time.LocalDateTime

data class WalletApiStateResponse(
    val id: String,
    val address: String,
    val webhook: String,
    val blockchain: String,
    val nonce: Int,
    val lastUpdateDate: LocalDateTime
)
