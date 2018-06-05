package io.openfuture.api.domain.scaffold

import javax.validation.Valid
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotEmpty

/**
 * @author Kadach Alexey
 */
data class CompileScaffoldRequest(
        @field:NotBlank var openKey: String? = null,
        @field:NotEmpty @field:Valid var properties: List<ScaffoldPropertyDto> = listOf()
)