package io.openfuture.api.domain.scaffold

import javax.validation.constraints.NotBlank

data class UpdateEthereumScaffoldRequest(
        @field:NotBlank var description: String? = null
)
