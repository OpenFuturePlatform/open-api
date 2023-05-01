package io.openfuture.keymanagementservice.dto

import io.openfuture.api.entity.application.BlockchainType

data class GenerateMultipleWalletForOrderRequest(
    val applicationId: String,
    val userId: String,
    val orderKey: String,
    val blockchains: List<BlockchainType>
)
