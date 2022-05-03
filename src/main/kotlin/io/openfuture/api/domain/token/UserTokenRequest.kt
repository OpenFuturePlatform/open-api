package io.openfuture.api.domain.token

import javax.validation.constraints.NotBlank

data class UserTokenRequest(
    @field:NotBlank val name: String,
    val symbol: String,
    val decimal: Int,
    @field:NotBlank val address: String
)