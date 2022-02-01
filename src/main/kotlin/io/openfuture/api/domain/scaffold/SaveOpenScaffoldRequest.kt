package io.openfuture.api.domain.scaffold

import javax.validation.constraints.NotBlank

data class SaveOpenScaffoldRequest(
        @field:NotBlank var developerAddress: String,
        @field:NotBlank var description: String,
        var webHook: String? = null
)