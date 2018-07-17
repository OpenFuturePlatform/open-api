package io.openfuture.api.component.scaffold.processor

import io.openfuture.api.domain.scaffold.CompiledScaffoldDto
import io.openfuture.api.domain.scaffold.DeployScaffoldRequest
import io.openfuture.api.domain.scaffold.ScaffoldPropertyDto
import io.openfuture.api.entity.scaffold.Scaffold
import io.openfuture.api.entity.scaffold.ScaffoldSummary
import io.openfuture.api.entity.scaffold.ScaffoldVersion
import io.openfuture.api.entity.scaffold.ShareHolder

interface VersionedScaffoldProcessor {

    fun getVersion(): ScaffoldVersion

    fun compile(properties: List<ScaffoldPropertyDto>): CompiledScaffoldDto

    fun deploy(data: String, request: DeployScaffoldRequest): String

    fun deactivate(scaffold: Scaffold)

    fun addShareHolder(scaffold: Scaffold, address: String, percent: Long)

    fun updateShareHolder(scaffold: Scaffold, address: String, percent: Long)

    fun removeShareHolder(scaffold: Scaffold, address: String)

    fun getScaffoldSummary(scaffold: Scaffold): ScaffoldSummary

    fun getShareHolders(summary: ScaffoldSummary): List<ShareHolder>

}