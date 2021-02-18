package io.openfuture.api.domain.state

import io.openfuture.api.entity.state.Blockchain
import java.time.LocalDateTime

data class StateWalletDto(
        val id: String,
        val address: String,
        val webhook: String,
        val blockchain: Blockchain,
        val lastUpdateDate: LocalDateTime
)
