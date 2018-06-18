package io.openfuture.api.util

object HexUtils {

    fun decode(hex: String): String = hex.substring(2)

    fun encode(value: String): String = "0x$value"

}