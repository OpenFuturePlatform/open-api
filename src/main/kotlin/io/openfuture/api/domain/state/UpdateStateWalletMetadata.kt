package io.openfuture.api.domain.state

data class UpdateStateWalletMetadata(
    val webhook: String,
    val applicationId: String,
    val metadata: WalletMetaData
)
