package io.openfuture.api.domain.state

import io.openfuture.api.domain.key.KeyWalletDto
import io.openfuture.api.domain.key.WalletMetaDto
import io.openfuture.api.entity.state.Blockchain

data class CreateStateWalletRequestMetadata(
    val webhook: String,
    val blockchains: List<KeyWalletDto>,
    val metadata: WalletMetaData
)
