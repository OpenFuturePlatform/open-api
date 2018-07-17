package io.openfuture.api.domain.scaffold

import io.openfuture.api.domain.auth.UserDto
import io.openfuture.api.entity.scaffold.Currency
import io.openfuture.api.entity.scaffold.Scaffold
import io.openfuture.api.entity.scaffold.ScaffoldVersion

data class ScaffoldDto(
        val address: String,
        val user: UserDto,
        val abi: String,
        val description: String,
        val fiatAmount: String,
        val currency: Currency,
        val conversionAmount: String,
        val developerAddress: String,
        val webHook: String?,
        val properties: List<ScaffoldPropertyDto>,
        val version: ScaffoldVersion
) {

    constructor(scaffold: Scaffold) : this(
            scaffold.address,
            UserDto(scaffold.openKey.user),
            scaffold.abi,
            scaffold.description,
            scaffold.fiatAmount,
            scaffold.getCurrency(),
            scaffold.conversionAmount,
            scaffold.developerAddress,
            scaffold.webHook,
            scaffold.property.map { ScaffoldPropertyDto(it) },
            scaffold.getVersion()
    )

}