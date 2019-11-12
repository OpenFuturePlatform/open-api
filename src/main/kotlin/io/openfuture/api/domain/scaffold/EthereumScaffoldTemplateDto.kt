package io.openfuture.api.domain.scaffold

import io.openfuture.api.entity.scaffold.Currency
import io.openfuture.api.entity.scaffold.EthereumScaffoldTemplate

class EthereumScaffoldTemplateDto(
        val name: String,
        val developerAddress: String?,
        val description: String?,
        val fiatAmount: String?,
        val currency: Currency?,
        val conversionAmount: String?,
        val webHook: String?,
        val properties: List<EthereumScaffoldTemplatePropertyDto>
) {

    constructor(ethereumScaffoldTemplate: EthereumScaffoldTemplate) : this(
            ethereumScaffoldTemplate.name,
            ethereumScaffoldTemplate.developerAddress,
            ethereumScaffoldTemplate.description,
            ethereumScaffoldTemplate.fiatAmount,
            ethereumScaffoldTemplate.getCurrency(),
            ethereumScaffoldTemplate.conversionAmount,
            ethereumScaffoldTemplate.webHook,
            ethereumScaffoldTemplate.property.map { EthereumScaffoldTemplatePropertyDto(it) }
    )

}