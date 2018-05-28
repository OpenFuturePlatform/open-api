package io.zensoft.open.api.domain.exception

import com.fasterxml.jackson.annotation.JsonInclude
import org.springframework.validation.FieldError
import org.springframework.validation.ObjectError

/**
 * @author Kadach Alexey
 */
@Suppress("MemberVisibilityCanPrivate")
@JsonInclude(JsonInclude.Include.NON_NULL)
data class ErrorDto(
        val code: String,
        var field: String? = null
) {
    constructor(error: ObjectError) : this(error.code ?: "500") {
        if (error is FieldError) {
            field = error.field
        }
    }
}