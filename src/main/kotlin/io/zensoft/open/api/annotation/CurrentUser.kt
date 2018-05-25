package io.zensoft.open.api.annotation

import kotlin.annotation.AnnotationRetention.RUNTIME
import kotlin.annotation.AnnotationTarget.CLASS
import kotlin.annotation.AnnotationTarget.VALUE_PARAMETER

/**
 * @author Kadach Alexey
 */
@Target(VALUE_PARAMETER, CLASS)
@Retention(RUNTIME)
annotation class CurrentUser