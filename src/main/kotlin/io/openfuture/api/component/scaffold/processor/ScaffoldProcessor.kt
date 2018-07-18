package io.openfuture.api.component.scaffold.processor

import io.openfuture.api.domain.scaffold.CompileScaffoldRequest
import io.openfuture.api.domain.scaffold.CompiledScaffoldDto
import io.openfuture.api.domain.scaffold.DeployScaffoldRequest
import io.openfuture.api.entity.scaffold.Scaffold
import io.openfuture.api.entity.scaffold.ScaffoldSummary
import io.openfuture.api.entity.scaffold.ScaffoldVersion
import io.openfuture.api.entity.scaffold.ShareHolder
import org.springframework.stereotype.Component

@Component
class ScaffoldProcessor(
        private val processors: List<VersionedScaffoldProcessor>
) {

    fun compile(request: CompileScaffoldRequest): CompiledScaffoldDto =
            getProcessor(request.version).compile(request.properties)

    fun deploy(data: String, request: DeployScaffoldRequest): String =
            getProcessor(request.version).deploy(data, request)

    fun deactivate(scaffold: Scaffold) = getProcessor(scaffold.getVersion()).deactivate(scaffold)

    fun activate(scaffold: Scaffold) = getProcessor(scaffold.getVersion()).activate(scaffold)

    fun addShareHolder(scaffold: Scaffold, address: String, percent: Long) =
            getProcessor(scaffold.getVersion()).addShareHolder(scaffold, address, percent)

    fun updateShareHolder(scaffold: Scaffold, address: String, percent: Long) =
            getProcessor(scaffold.getVersion()).updateShareHolder(scaffold, address, percent)

    fun removeShareHolder(scaffold: Scaffold, address: String) =
            getProcessor(scaffold.getVersion()).removeShareHolder(scaffold, address)

    fun getScaffoldSummary(scaffold: Scaffold): ScaffoldSummary =
            getProcessor(scaffold.getVersion()).getScaffoldSummary(scaffold)

    fun getShareHolders(summary: ScaffoldSummary): List<ShareHolder> =
            getProcessor(summary.scaffold.getVersion()).getShareHolders(summary)

    private fun getProcessor(version: ScaffoldVersion): VersionedScaffoldProcessor = processors.firstOrNull { version == it.getVersion() }
            ?: throw IllegalArgumentException("Illegal scaffold version $version")

}