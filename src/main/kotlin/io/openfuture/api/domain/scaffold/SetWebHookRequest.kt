package io.openfuture.api.domain.scaffold

import javax.validation.constraints.Pattern

data class SetWebHookRequest(
        @field:Pattern(regexp = "^(https?://)([\\w.]+)\\.([a-z]{2,6}\\.?)(/[\\w.]+)+/?\$") var webHook: String? = null
)