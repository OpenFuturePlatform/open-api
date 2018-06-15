package io.openfuture.api.config

import org.mockito.Mockito

/**
 * @author Yauheni Efimenko
 */
fun <T> any(clazz: Class<T>): T = Mockito.any<T>(clazz)

fun <T> anyList(clazz: Class<T>): List<T>  = Mockito.anyListOf(clazz)

fun anyLong(): Long = Mockito.anyLong()

fun anyInt(): Int = Mockito.anyInt()

fun anyString(): String = Mockito.anyString()

fun anyObject(): Any = Mockito.anyObject()

fun <T> eq(any: T): T = Mockito.eq<T>(any)