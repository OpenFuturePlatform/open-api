package io.openfuture.api.domain.key

data class KeyWalletDto(
    val address: String,
    val blockchain: String,
    val walletType: String,
    val encrypted: String
)
