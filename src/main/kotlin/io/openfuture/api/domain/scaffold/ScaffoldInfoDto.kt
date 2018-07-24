package io.openfuture.api.domain.scaffold

import io.openfuture.api.entity.scaffold.Currency
import io.openfuture.api.entity.scaffold.Scaffold

class ScaffoldInfoDto(
        val address: String,
        val description: String,
        val fiatAmount: String,
        val currency: Currency,
        val conversionAmount: String,
        val properties: List<ScaffoldPropertyDto>
) {

    constructor(scaffold: Scaffold) : this(
            scaffold.address,
            scaffold.description,
            scaffold.fiatAmount,
            scaffold.getCurrency(),
            scaffold.conversionAmount,
            scaffold.property.map { ScaffoldPropertyDto(it) }
    )

}