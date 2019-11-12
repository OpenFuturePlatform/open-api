package io.openfuture.api.entity.scaffold

import io.openfuture.api.entity.base.Dictionary

enum class Blockchain(
        private val id: Int,
        private val value: String
) : Dictionary {

    Ethereum(1, "Ethereum"),
    OPEN(2, "OPEN Chain")
    ;

    override fun getId(): Int = id

    fun getValue(): String = value

}