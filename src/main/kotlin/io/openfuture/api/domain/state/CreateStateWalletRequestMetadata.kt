package io.openfuture.api.domain.state

import io.openfuture.api.domain.key.BlockchainData

data class CreateStateWalletRequestMetadata(
    val applicationId: String,
    val blockchains: List<BlockchainData>,
    val metadata: WalletMetaData,
    val webhook: String
)
