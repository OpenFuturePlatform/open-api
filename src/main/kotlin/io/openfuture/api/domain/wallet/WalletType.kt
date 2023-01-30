package io.openfuture.api.domain.wallet

import com.fasterxml.jackson.annotation.JsonValue

enum class WalletType(
    private val value: String
) {
    CUSTODIAL("CUSTODIAL"),
    NONCUSTODIAL("NONCUSTODIAL");

    @JsonValue
    fun getValue(): String = value
}