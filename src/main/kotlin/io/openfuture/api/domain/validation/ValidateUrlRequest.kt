package io.openfuture.api.domain.validation

import io.openfuture.api.annotation.Url

data class ValidateUrlRequest(
        @Url var url: String? = null
)