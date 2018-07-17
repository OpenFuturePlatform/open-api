package io.openfuture.api.domain.scaffold

import io.openfuture.api.entity.scaffold.ScaffoldVersion
import javax.validation.Valid
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

data class CompileScaffoldRequest(
        @field:NotBlank var openKey: String? = null,
        @field:NotEmpty @field:Size(max = 9) @field:Valid var properties: List<ScaffoldPropertyDto> = listOf(),
        @field:NotNull var version: ScaffoldVersion = ScaffoldVersion.last()
)