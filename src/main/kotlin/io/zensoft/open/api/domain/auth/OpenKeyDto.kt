package io.zensoft.open.api.domain.auth

import io.zensoft.open.api.model.auth.OpenKey
import java.util.*

/**
 * @author Kadach Alexey
 */
class OpenKeyDto(
        val value: String,
        val enabled: Boolean,
        val expiredDate: Date?
) {

    constructor(openKey: OpenKey) : this(openKey.value, openKey.enabled, openKey.expiredDate)

}