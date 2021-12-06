package io.openfuture.api.domain.application

import javax.validation.constraints.*

data class ApplicationRequest(
    @field:NotBlank var name: String? = null,
    var active: Boolean = true,
    var webHook: String? = null
)
