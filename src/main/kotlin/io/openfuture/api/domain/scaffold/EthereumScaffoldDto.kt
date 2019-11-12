package io.openfuture.api.domain.scaffold

import io.openfuture.api.domain.auth.UserDto
import io.openfuture.api.entity.scaffold.Currency
import io.openfuture.api.entity.scaffold.EthereumScaffold
import io.openfuture.api.entity.scaffold.ScaffoldVersion

data class EthereumScaffoldDto(
        val address: String,
        val user: UserDto,
        val abi: String,
        val description: String,
        val fiatAmount: String,
        val currency: Currency,
        val conversionAmount: String,
        val developerAddress: String,
        val webHook: String?,
        val properties: List<EthereumScaffoldPropertyDto>,
        val version: ScaffoldVersion
) {

    constructor(ethereumScaffold: EthereumScaffold) : this(
            ethereumScaffold.address,
            UserDto(ethereumScaffold.openKey.user),
            ethereumScaffold.abi,
            ethereumScaffold.description,
            ethereumScaffold.fiatAmount,
            ethereumScaffold.getCurrency(),
            ethereumScaffold.conversionAmount,
            ethereumScaffold.developerAddress,
            ethereumScaffold.webHook,
            ethereumScaffold.property.map { EthereumScaffoldPropertyDto(it) },
            ethereumScaffold.getVersion()
    )

}