package io.openfuture.api.domain.state

data class WalletDto(
        val id: Long,
        val address: String,
        val balance: Double,
        val currency: String,
        val lastUpdated: Long,
        val startTrackingDate: Long
)
