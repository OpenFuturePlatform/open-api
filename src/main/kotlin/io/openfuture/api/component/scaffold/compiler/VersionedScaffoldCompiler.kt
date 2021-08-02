package io.openfuture.api.component.scaffold.compiler

import io.openfuture.api.component.solidity.CompilationResult
import io.openfuture.api.domain.scaffold.EthereumScaffoldPropertyDto
import io.openfuture.api.entity.scaffold.ScaffoldVersion

interface VersionedScaffoldCompiler {

    fun getVersion(): ScaffoldVersion

    fun compile(properties: List<EthereumScaffoldPropertyDto>): CompilationResult.ContractMetadata

}