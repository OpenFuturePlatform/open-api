package io.openfuture.api.domain.scaffold

import io.openfuture.api.annotation.Url

data class SetWebHookRequest(
        @Url var webHook: String
)