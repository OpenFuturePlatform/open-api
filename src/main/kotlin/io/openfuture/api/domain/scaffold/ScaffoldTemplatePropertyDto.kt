package io.openfuture.api.domain.scaffold

import io.openfuture.api.entity.scaffold.PropertyType
import io.openfuture.api.entity.scaffold.ScaffoldTemplateProperty

/**
 * @author Kadach Alexey
 */
class ScaffoldTemplatePropertyDto(
        var name: String? = null,
        var type: PropertyType? = null,
        var defaultValue: String? = null
) {

    constructor(property: ScaffoldTemplateProperty) : this(
            property.name,
            property.getType(),
            property.defaultValue
    )

}