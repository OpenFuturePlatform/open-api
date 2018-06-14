package io.openfuture.api.domain.scaffold

import io.openfuture.api.entity.scaffold.Currency
import javax.validation.Valid
import javax.validation.constraints.NotBlank

/**
 * @author Kadach Alexey
 */
class SaveScaffoldTemplateRequest(
        @field:NotBlank var name: String? = null,
        var developerAddress: String? = null,
        var description: String? = null,
        var fiatAmount: String? = null,
        var currency: Currency? = null,
        var conversionAmount: String? = null,
        var webHook: String? = null,
        @field:Valid var properties: List<ScaffoldTemplatePropertyDto> = listOf()
)