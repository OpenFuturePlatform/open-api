package io.openfuture.api.util

import java.util.*

fun getRandomNumber(from: Int, to: Int): Int {
    val a = from + (to - from) * Random().nextFloat()
    val b = a.toInt()
    return (if (a - b > 0.5) 1 else 0) + b
}

fun getOrderKey(prefix:String):String{
    val body = System.currentTimeMillis().toString()
    return prefix + body + getRandomNumber(10, 99)
}