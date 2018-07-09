package io.openfuture.api.config

import org.mockito.Mockito

fun <T> any(clazz: Class<T>): T = Mockito.any<T>(clazz)

fun anyLong(): Long = Mockito.anyLong()

fun anyInt(): Int = Mockito.anyInt()

fun anyString(): String = Mockito.anyString()

fun <T> eq(any: T): T = Mockito.eq<T>(any)
