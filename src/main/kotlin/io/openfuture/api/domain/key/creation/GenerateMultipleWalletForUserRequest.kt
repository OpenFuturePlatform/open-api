package io.openfuture.keymanagementservice.dto

import io.openfuture.api.entity.application.BlockchainType

data class GenerateMultipleWalletForUserRequest(
    val applicationId: String,
    val applicationUserId: String,//user id of the Open Platform
    var userId: String,//user of the application
    val blockchains: List<BlockchainType>
)
