package io.openfuture.api.domain.scaffold

import io.openfuture.api.entity.scaffold.PropertyType
import io.openfuture.api.entity.scaffold.ScaffoldProperty
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

data class ScaffoldPropertyDto(
        @field:NotBlank var name: String? = null,
        @field:NotNull var type: PropertyType? = null,
        var defaultValue: String? = null
) {

    constructor(property: ScaffoldProperty) : this(
            property.name,
            property.getType(),
            property.defaultValue
    )

}