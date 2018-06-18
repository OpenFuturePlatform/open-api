package io.openfuture.api.domain.scaffold

import javax.validation.constraints.NotBlank

/**
 * @author Alexey Skadorva
 */
data class UpdateScaffoldRequest(
        @field:NotBlank var description: String? = null
)
