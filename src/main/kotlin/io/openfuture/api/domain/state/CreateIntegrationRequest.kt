package io.openfuture.api.domain.state

data class CreateIntegrationRequest(
        val address: String,
        val blockchainId: Int
)
