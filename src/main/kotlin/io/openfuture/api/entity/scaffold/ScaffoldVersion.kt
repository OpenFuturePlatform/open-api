package io.openfuture.api.entity.scaffold

import io.openfuture.api.entity.base.Dictionary

enum class ScaffoldVersion(
        private val id: Int
) : Dictionary {

    V1(1)
    ;

    override fun getId(): Int = id

    companion object {
        fun last(): ScaffoldVersion = V1
    }

}