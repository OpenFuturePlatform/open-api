package io.openfuture.api.domain.state

import io.openfuture.api.domain.key.KeyWalletDto

data class CreateStateWithOrderRequest(
    val webhook: String,
    val applicationId: String,
    val blockchains: List<KeyWalletDto>,
    val metadata: OrderMetaDataV2
)

data class CreateStateWithUserRequest(
    val id: String,
    val webhook: String,
    val blockchains: List<KeyWalletDto>,
    val applicationId: String,
    val userId: String,
    val test: Boolean,
    val metadata: Any
)

data class OrderMetaDataV2(
    var amount: String,
    var orderKey: String,
    var productCurrency: String,
    var source: String,
    var test: Boolean
)

