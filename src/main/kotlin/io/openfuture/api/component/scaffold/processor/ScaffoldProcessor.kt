package io.openfuture.api.component.scaffold.processor

import io.openfuture.api.domain.scaffold.CompileEthereumScaffoldRequest
import io.openfuture.api.domain.scaffold.CompiledScaffoldDto
import io.openfuture.api.domain.scaffold.DeployEthereumScaffoldRequest
import io.openfuture.api.entity.scaffold.EthereumScaffold
import io.openfuture.api.entity.scaffold.EthereumScaffoldSummary
import io.openfuture.api.entity.scaffold.EthereumShareHolder
import io.openfuture.api.entity.scaffold.ScaffoldVersion
import org.springframework.stereotype.Component

@Component
class ScaffoldProcessor(
        private val processors: List<VersionedScaffoldProcessor>
) {

    fun compile(request: CompileEthereumScaffoldRequest): CompiledScaffoldDto =
            getProcessor(request.version).compile(request.properties)

    fun deploy(data: String?, request: DeployEthereumScaffoldRequest): String =
            getProcessor(request.version).deploy(data, request)

    fun deactivate(ethereumScaffold: EthereumScaffold) = getProcessor(ethereumScaffold.getVersion()).deactivate(ethereumScaffold)

    fun activate(ethereumScaffold: EthereumScaffold) = getProcessor(ethereumScaffold.getVersion()).activate(ethereumScaffold)

    fun addShareHolder(ethereumScaffold: EthereumScaffold, address: String, percent: Long) =
            getProcessor(ethereumScaffold.getVersion()).addShareHolder(ethereumScaffold, address, percent)

    fun updateShareHolder(ethereumScaffold: EthereumScaffold, address: String, percent: Long) =
            getProcessor(ethereumScaffold.getVersion()).updateShareHolder(ethereumScaffold, address, percent)

    fun removeShareHolder(ethereumScaffold: EthereumScaffold, address: String) =
            getProcessor(ethereumScaffold.getVersion()).removeShareHolder(ethereumScaffold, address)

    fun getScaffoldSummary(ethereumScaffold: EthereumScaffold): EthereumScaffoldSummary =
            getProcessor(ethereumScaffold.getVersion()).getScaffoldSummary(ethereumScaffold)

    fun getShareHolders(summary: EthereumScaffoldSummary): List<EthereumShareHolder> =
            getProcessor(summary.ethereumScaffold.getVersion()).getShareHolders(summary)

    private fun getProcessor(version: ScaffoldVersion): VersionedScaffoldProcessor = processors.firstOrNull { version == it.getVersion() }
            ?: throw IllegalArgumentException("Illegal scaffold version $version")

}