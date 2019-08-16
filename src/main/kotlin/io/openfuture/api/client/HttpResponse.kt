package io.openfuture.api.client

data class HttpResponse(
        val headers: Map<String, String>,
        val entity: String
)
