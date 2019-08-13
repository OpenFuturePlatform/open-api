package io.openfuture.api.domain.state

data class AccountDto(
        val id: Long,
        val webHook: String,
        val isEnabled: Boolean,
        val walletsCount: Int
)
