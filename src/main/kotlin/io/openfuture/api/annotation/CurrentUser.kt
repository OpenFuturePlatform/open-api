package io.openfuture.api.annotation

import org.springframework.security.core.annotation.AuthenticationPrincipal
import kotlin.annotation.AnnotationRetention.RUNTIME
import kotlin.annotation.AnnotationTarget.CLASS
import kotlin.annotation.AnnotationTarget.VALUE_PARAMETER

/**
 * @author Kadach Alexey
 */
@Target(VALUE_PARAMETER, CLASS)
@Retention(RUNTIME)
@AuthenticationPrincipal
annotation class CurrentUser