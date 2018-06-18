package io.openfuture.api.domain.scaffold

data class ScaffoldQuotaDto(
        val currentCount: Long,
        val limitCount: Long
)