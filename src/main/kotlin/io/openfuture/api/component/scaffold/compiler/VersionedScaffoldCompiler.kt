package io.openfuture.api.component.scaffold.compiler

import io.openfuture.api.domain.scaffold.EthereumScaffoldPropertyDto
import io.openfuture.api.entity.scaffold.ScaffoldVersion
import org.ethereum.solidity.compiler.CompilationResult.ContractMetadata

interface VersionedScaffoldCompiler {

    fun getVersion(): ScaffoldVersion

    fun compile(properties: List<EthereumScaffoldPropertyDto>): ContractMetadata

}