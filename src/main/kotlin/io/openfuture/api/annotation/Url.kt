package io.openfuture.api.annotation

import javax.validation.Constraint
import javax.validation.constraints.Pattern
import kotlin.annotation.AnnotationRetention.RUNTIME
import kotlin.annotation.AnnotationTarget.FIELD
import kotlin.annotation.AnnotationTarget.TYPE_PARAMETER
import kotlin.reflect.KClass

@Constraint(validatedBy = [])
@Target(FIELD, TYPE_PARAMETER)
@Retention(RUNTIME)
@Pattern(regexp = "^(https?://)([\\w.]+)\\.([a-z]{2,6}\\.?)(/[\\w.]+)+/?\$")
annotation class Url(
    val message: String = "Field is not match to url pattern",
    val groups: Array<KClass<out Any>> = [],
    val payload: Array<KClass<out Any>> = []
)