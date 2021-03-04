package io.openfuture.api.entity.state

import com.fasterxml.jackson.annotation.JsonValue

enum class Blockchain(private val value: String) {

    Ethereum("EthereumBlockchain");

    @JsonValue
    fun getValue(): String = value

}
