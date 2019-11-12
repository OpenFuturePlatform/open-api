package io.openfuture.api.annotation

import io.openfuture.api.annotation.validator.AddressValidator
import javax.validation.Constraint
import kotlin.annotation.AnnotationRetention.RUNTIME
import kotlin.annotation.AnnotationTarget.*
import kotlin.reflect.KClass

@Constraint(validatedBy = [AddressValidator::class])
@Target(FIELD, TYPE_PARAMETER, VALUE_PARAMETER)
@Retention(RUNTIME)
annotation class Address(
        val message: String = "Field is not match to address",
        val groups: Array<KClass<out Any>> = [],
        val payload: Array<KClass<out Any>> = []
)