package io.openfuture.api.domain.validation

import io.openfuture.api.annotation.Url
import javax.validation.constraints.NotNull

data class ValidateUrlRequest(
        @field:NotNull @Url var url: String? = null
)