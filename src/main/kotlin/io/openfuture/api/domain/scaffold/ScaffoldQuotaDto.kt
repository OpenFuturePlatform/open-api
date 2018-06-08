package io.openfuture.api.domain.scaffold

/**
 * @author Kadach Alexey
 */
data class ScaffoldQuotaDto(
        val currentCount: Long,
        val limitCount: Long
)