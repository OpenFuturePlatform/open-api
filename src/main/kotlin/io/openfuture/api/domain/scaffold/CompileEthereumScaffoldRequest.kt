package io.openfuture.api.domain.scaffold

import io.openfuture.api.entity.scaffold.ScaffoldVersion
import javax.validation.Valid
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

data class CompileEthereumScaffoldRequest(
        @field:NotEmpty @field:Size(max = 9) @field:Valid var properties: List<EthereumScaffoldPropertyDto> = listOf(),
        @field:NotNull var version: ScaffoldVersion = ScaffoldVersion.last()
)