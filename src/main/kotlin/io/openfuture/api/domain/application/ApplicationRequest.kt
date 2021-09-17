package io.openfuture.api.domain.application

import io.openfuture.api.entity.scaffold.Currency
import javax.validation.constraints.*

data class ApplicationRequest(
    @field:NotBlank var name: String? = null,
    var expirationPeriod: Int? = null,
    var currency: Currency? = null,
    var active: Boolean = true,
    var webHook: String? = null
)
