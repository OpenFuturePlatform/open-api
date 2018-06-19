package io.openfuture.api.domain.scaffold

import javax.validation.constraints.NotBlank

data class RemoveShareHolderRequest(
        @field:NotBlank var address: String? = null
)