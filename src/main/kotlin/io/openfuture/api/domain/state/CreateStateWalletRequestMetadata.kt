package io.openfuture.api.domain.state

import io.openfuture.api.domain.key.KeyWalletDto

data class CreateStateWalletRequestMetadata(
    val webhook: String,
    val applicationId: String,
    val blockchains: List<KeyWalletDto>,
    val metadata: WalletMetaData
)
