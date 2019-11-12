package io.openfuture.api.domain.state

data class OpenScaffoldDto(
        val id: Long,
        val recipientAddress: String,
        val webHook: String
)