package io.openfuture.api.domain.holder

import io.openfuture.api.annotation.Address
import javax.validation.constraints.Max
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

data class AddEthereumShareHolderRequest(
        @field:NotBlank @field:Address var address: String? = null,
        @field:NotNull @field:Min(0) @field:Max(100) var percent: Int? = null
)