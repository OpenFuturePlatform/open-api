package io.openfuture.api.domain.state

data class AddWalletRequest(
        val integrations: Set<CreateIntegrationRequest> = setOf()
)
