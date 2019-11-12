package io.openfuture.api.domain.scaffold

import io.openfuture.api.entity.scaffold.EthereumScaffoldTemplateProperty
import io.openfuture.api.entity.scaffold.PropertyType

class EthereumScaffoldTemplatePropertyDto(
        var name: String? = null,
        var type: PropertyType? = null,
        var defaultValue: String? = null
) {

    constructor(property: EthereumScaffoldTemplateProperty) : this(
            property.name,
            property.getType(),
            property.defaultValue
    )

}