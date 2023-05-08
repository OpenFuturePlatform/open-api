package io.openfuture.api.domain.key.creation

import io.openfuture.api.entity.application.BlockchainType

data class GenerateMultipleWalletRequest(
    val applicationId: String,
    val applicationUserId: String,
    var uniqueId: String,
    val blockchains: List<BlockchainType>
)
