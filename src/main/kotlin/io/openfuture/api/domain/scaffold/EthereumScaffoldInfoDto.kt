package io.openfuture.api.domain.scaffold

import io.openfuture.api.entity.scaffold.Currency
import io.openfuture.api.entity.scaffold.EthereumScaffold

class EthereumScaffoldInfoDto(
        val address: String,
        val abi: String,
        val description: String,
        val fiatAmount: String,
        val currency: Currency,
        val conversionAmount: String,
        val properties: List<EthereumScaffoldPropertyDto>
) {

    constructor(ethereumScaffold: EthereumScaffold) : this(
            ethereumScaffold.address,
            ethereumScaffold.abi,
            ethereumScaffold.description,
            ethereumScaffold.fiatAmount,
            ethereumScaffold.getCurrency(),
            ethereumScaffold.conversionAmount,
            ethereumScaffold.property.map { EthereumScaffoldPropertyDto(it) }
    )

}