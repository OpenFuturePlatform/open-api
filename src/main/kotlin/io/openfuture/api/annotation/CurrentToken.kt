package io.openfuture.api.annotation

import org.springframework.security.core.annotation.AuthenticationPrincipal
import kotlin.annotation.AnnotationRetention.RUNTIME
import kotlin.annotation.AnnotationTarget.VALUE_PARAMETER

@Target(VALUE_PARAMETER)
@Retention(RUNTIME)
@AuthenticationPrincipal
annotation class CurrentToken