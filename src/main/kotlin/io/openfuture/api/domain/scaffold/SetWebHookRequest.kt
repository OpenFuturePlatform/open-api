package io.openfuture.api.domain.scaffold

import org.hibernate.validator.constraints.URL
import javax.validation.constraints.NotBlank

data class SetWebHookRequest(
        @field:NotBlank @field:URL var webHook: String? = null
)