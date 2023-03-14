package io.openfuture.api.domain.key

import io.openfuture.api.entity.application.BlockchainType

class ImportWalletDataRequest(
    val applicationId: String,
    val userId: String,
    val orderId: String,
    val address: String,
    val encryptedPrivateKey: String,
    val blockchainType: BlockchainType
)