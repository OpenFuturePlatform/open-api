package io.openfuture.api.domain.scaffold

import javax.validation.constraints.Max
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

data class AddShareHolderRequest(
        @field:NotBlank var address: String? = null,
        @field:NotNull @field:Min(0) @field:Max(100) var percent: Int
)