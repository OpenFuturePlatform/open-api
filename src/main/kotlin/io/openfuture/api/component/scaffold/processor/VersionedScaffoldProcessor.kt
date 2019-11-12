package io.openfuture.api.component.scaffold.processor

import io.openfuture.api.domain.scaffold.CompiledScaffoldDto
import io.openfuture.api.domain.scaffold.DeployEthereumScaffoldRequest
import io.openfuture.api.domain.scaffold.EthereumScaffoldPropertyDto
import io.openfuture.api.entity.scaffold.EthereumScaffold
import io.openfuture.api.entity.scaffold.EthereumScaffoldSummary
import io.openfuture.api.entity.scaffold.EthereumShareHolder
import io.openfuture.api.entity.scaffold.ScaffoldVersion

interface VersionedScaffoldProcessor {

    fun getVersion(): ScaffoldVersion

    fun compile(properties: List<EthereumScaffoldPropertyDto>): CompiledScaffoldDto

    fun deploy(data: String, request: DeployEthereumScaffoldRequest): String

    fun deactivate(ethereumScaffold: EthereumScaffold)

    fun activate(ethereumScaffold: EthereumScaffold)

    fun addShareHolder(ethereumScaffold: EthereumScaffold, address: String, percent: Long)

    fun updateShareHolder(ethereumScaffold: EthereumScaffold, address: String, percent: Long)

    fun removeShareHolder(ethereumScaffold: EthereumScaffold, address: String)

    fun getScaffoldSummary(ethereumScaffold: EthereumScaffold): EthereumScaffoldSummary

    fun getShareHolders(summary: EthereumScaffoldSummary): List<EthereumShareHolder>

}