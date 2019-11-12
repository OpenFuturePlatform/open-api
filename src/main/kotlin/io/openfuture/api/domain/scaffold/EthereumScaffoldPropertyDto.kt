package io.openfuture.api.domain.scaffold

import io.openfuture.api.entity.scaffold.EthereumScaffoldProperty
import io.openfuture.api.entity.scaffold.PropertyType
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

data class EthereumScaffoldPropertyDto(
        @field:NotBlank var name: String? = null,
        @field:NotNull var type: PropertyType? = null,
        var defaultValue: String? = null
) {

    constructor(property: EthereumScaffoldProperty) : this(
            property.name,
            property.getType(),
            property.defaultValue
    )

}