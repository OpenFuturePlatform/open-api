package io.openfuture.api.entity.application

import io.openfuture.api.entity.base.Dictionary

enum class BlockchainType(
    private val id: Int,
    private val value: String
): Dictionary {
    ETH(1, "ETH"),
    BTC(2, "BTC"),
    BNB(3, "BNB");

    override fun getId(): Int = id

    fun getValue(): String = value
}