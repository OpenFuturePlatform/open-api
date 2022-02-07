package io.openfuture.api.domain.state

import io.openfuture.api.domain.key.WalletMetaDto
import io.openfuture.api.entity.state.Blockchain

data class CreateStateWalletRequestMetadata(
    val address: String,
    val webhook: String,
    val blockchain: Blockchain,
    val metadata: WalletMetaDto?
)
