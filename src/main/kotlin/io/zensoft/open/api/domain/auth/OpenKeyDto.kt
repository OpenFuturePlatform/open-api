package io.zensoft.open.api.domain.auth

import io.zensoft.open.api.model.OpenKey

/**
 * @author Kadach Alexey
 */
class OpenKeyDto(
        val value: String
) {

    constructor(openKey: OpenKey) : this(openKey.value)

}