package io.openfuture.api.domain.state

data class AccountDto(
        val id: Long,
        val webHook: String,
        val enabled: Boolean,
        val walletsCount: Int
)
