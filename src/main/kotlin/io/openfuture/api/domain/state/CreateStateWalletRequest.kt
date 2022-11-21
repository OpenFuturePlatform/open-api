package io.openfuture.api.domain.state

import io.openfuture.api.entity.state.Blockchain

data class CreateStateWalletRequest(
        val address: String,
        val webhook: String,
        val blockchain: String,
        val applicationId: String
)
