package io.openfuture.api.domain.scaffold

import org.ethereum.solidity.compiler.CompilationResult

data class CompiledScaffoldDto(
        val abi: String,
        val bin: String
) {

    constructor(scaffold: CompilationResult.ContractMetadata) : this(scaffold.abi, scaffold.bin)

}