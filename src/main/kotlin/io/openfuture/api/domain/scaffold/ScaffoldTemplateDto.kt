package io.openfuture.api.domain.scaffold

import io.openfuture.api.domain.auth.UserDto
import io.openfuture.api.entity.scaffold.Currency
import io.openfuture.api.entity.scaffold.ScaffoldTemplate

/**
 * @author Kadach Alexey
 */
class ScaffoldTemplateDto(
        val id: Long,
        val name: String,
        val user: UserDto,
        val developerAddress: String?,
        val description: String?,
        val fiatAmount: String?,
        val currency: Currency?,
        val conversionAmount: String?,
        val properties: List<ScaffoldTemplatePropertyDto>
) {

    constructor(scaffold: ScaffoldTemplate) : this(
            scaffold.id,
            scaffold.name,
            UserDto(scaffold.user),
            scaffold.developerAddress,
            scaffold.description,
            scaffold.fiatAmount,
            scaffold.getCurrency(),
            scaffold.conversionAmount,
            scaffold.property.map { ScaffoldTemplatePropertyDto(it) }
    )

}