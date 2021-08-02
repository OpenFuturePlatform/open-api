package io.openfuture.api.domain.scaffold

import io.openfuture.api.component.solidity.CompilationResult

data class CompiledScaffoldDto(
    val abi: String?,
    val bin: String?
) {

    constructor(scaffold: CompilationResult.ContractMetadata) : this(scaffold.abi, scaffold.bin)

}