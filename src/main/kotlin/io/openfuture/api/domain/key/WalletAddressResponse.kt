package io.openfuture.api.domain.key

data class WalletAddressResponse(
    val blockchain: String,
    var userId: String,
    var applicationId: String
)
