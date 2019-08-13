package io.openfuture.api.domain.state

data class CreateAccountRequest(
        val webHook: String,
        val integrations: Set<CreateIntegrationRequest>
)
