package io.openfuture.api.domain.key

import io.openfuture.api.entity.application.BlockchainType

data class CreateKeyRequest(
    val applicationId: String,
    val userId: String,
    val blockchainType: BlockchainType
)