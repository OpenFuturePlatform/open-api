package io.openfuture.api.domain.state

data class CreateStateWalletRequest(
        val address: String,
        val applicationId: String,
        val blockchain: String,
        val webhook: String
)
