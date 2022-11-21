package io.openfuture.api.domain.key

import io.openfuture.api.entity.application.BlockchainType

data class ImportKeyRequest(
    val applicationId: String,
    val userId: String,
    val blockchainType: BlockchainType,
    val address: String,
    val hex: String
)
