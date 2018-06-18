package io.openfuture.api.domain.scaffold

import io.openfuture.api.domain.auth.UserDto
import io.openfuture.api.entity.scaffold.Currency
import io.openfuture.api.entity.scaffold.Scaffold

data class ScaffoldDto(
        val address: String,
        val user: UserDto,
        val abi: String,
        val developerAddress: String,
        val description: String,
        val fiatAmount: String,
        val currency: Currency,
        val conversionAmount: String,
        val properties: List<ScaffoldPropertyDto>,
        val enabled: Boolean
) {

    constructor(scaffold: Scaffold) : this(
            scaffold.address,
            UserDto(scaffold.openKey.user),
            scaffold.abi,
            scaffold.developerAddress,
            scaffold.description,
            scaffold.fiatAmount,
            scaffold.getCurrency(),
            scaffold.conversionAmount,
            scaffold.property.map { ScaffoldPropertyDto(it) },
            scaffold.enabled
    )

}