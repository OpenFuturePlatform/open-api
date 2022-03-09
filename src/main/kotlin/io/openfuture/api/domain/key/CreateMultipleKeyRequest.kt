package io.openfuture.api.domain.key

import io.openfuture.api.entity.application.BlockchainType

data class CreateMultipleKeyRequest(
    val applicationId: String,
    val userId: String,
    val orderKey: String,
    val blockchains: List<BlockchainType>
)
