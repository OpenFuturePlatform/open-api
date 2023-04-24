package io.openfuture.api.domain.key

import io.openfuture.api.entity.application.BlockchainType

data class GenerateWalletRequest(
    var applicationId: String,
    val blockchainType: BlockchainType,
    var webHook: String
)
