package io.openfuture.api.domain.scaffold

import io.openfuture.api.entity.scaffold.OpenScaffold

data class OpenScaffoldDto(
        val developerAddress: String,
        val description: String,
        val webHook: String?
) {

    constructor(openScaffold: OpenScaffold) : this(
            openScaffold.developerAddress,
            openScaffold.description,
            openScaffold.webHook
    )

}