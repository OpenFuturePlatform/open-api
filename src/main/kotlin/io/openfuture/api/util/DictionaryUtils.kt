package io.openfuture.api.util

import io.openfuture.api.entity.base.Dictionary

/**
 * @author Kadach Alexey
 */
object DictionaryUtils {

    @Suppress("UNCHECKED_CAST")
    fun <T : Enum<*>> valueOf(clazz: Class<out T>, id: Int): T {
        val values = clazz.enumConstants
        return values.firstOrNull { (it as Dictionary).getId() == id } ?: throw IllegalStateException("Type ID not found")
    }

}