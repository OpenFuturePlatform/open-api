package io.openfuture.api.domain.auth

import io.openfuture.api.entity.auth.OpenKey
import java.util.*

class OpenKeyDto(
        val value: String,
        val enabled: Boolean,
        val expiredDate: Date?
) {

    constructor(openKey: OpenKey) : this(openKey.value, openKey.enabled, openKey.expiredDate)

}