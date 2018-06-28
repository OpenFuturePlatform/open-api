package io.openfuture.api.domain.validation

import javax.validation.constraints.Pattern

data class ValidateUrlRequest(
        @field:Pattern(regexp = "^(https?://)([\\w.]+)\\.([a-z]{2,6}\\.?)(/[\\w.]+)+/?\$") var url: String? = null
)