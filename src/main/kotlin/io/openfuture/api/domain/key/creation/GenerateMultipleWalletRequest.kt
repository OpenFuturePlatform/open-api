package io.openfuture.keymanagementservice.dto

import io.openfuture.api.entity.application.BlockchainType

data class GenerateMultipleWalletRequest(
    val applicationId: String,
    val applicationUserId: String,
    var uniqueId: String,
    val blockchains: List<BlockchainType>
)
