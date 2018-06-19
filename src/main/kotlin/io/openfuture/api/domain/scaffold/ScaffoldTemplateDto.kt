package io.openfuture.api.domain.scaffold

import io.openfuture.api.entity.scaffold.Currency
import io.openfuture.api.entity.scaffold.ScaffoldTemplate

class ScaffoldTemplateDto(
        val name: String,
        val developerAddress: String?,
        val description: String?,
        val fiatAmount: String?,
        val currency: Currency?,
        val conversionAmount: String?,
        val webHook: String?,
        val properties: List<ScaffoldTemplatePropertyDto>
) {

    constructor(scaffold: ScaffoldTemplate) : this(
            scaffold.name,
            scaffold.developerAddress,
            scaffold.description,
            scaffold.fiatAmount,
            scaffold.getCurrency(),
            scaffold.conversionAmount,
            scaffold.webHook,
            scaffold.property.map { ScaffoldTemplatePropertyDto(it) }
    )

}