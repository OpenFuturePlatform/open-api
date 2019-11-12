package io.openfuture.api.domain.holder

import javax.validation.constraints.Max
import javax.validation.constraints.Min
import javax.validation.constraints.NotNull

data class UpdateEthereumShareHolderRequest(
        @field:NotNull @field:Min(0) @field:Max(100) var percent: Int? = null
)