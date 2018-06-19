package io.openfuture.api.entity.scaffold

import io.openfuture.api.entity.base.Dictionary

enum class PropertyType(
        private val id: Int,
        private val value: String
) : Dictionary {

    STRING(1, "string"),
    NUMBER(2, "uint"),
    BOOLEAN(3, "boolean")
    ;

    override fun getId(): Int = id

    fun getValue(): String = value

}