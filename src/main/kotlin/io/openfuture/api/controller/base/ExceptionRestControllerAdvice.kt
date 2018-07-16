package io.openfuture.api.controller.base

import io.openfuture.api.domain.exception.ErrorDto
import io.openfuture.api.domain.exception.ExceptionResponse
import io.openfuture.api.exception.CompileException
import io.openfuture.api.exception.ExecuteTransactionException
import io.openfuture.api.exception.FunctionCallException
import io.openfuture.api.exception.TemplateProcessingException
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ExceptionRestControllerAdvice {

    @ResponseStatus(code = BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun methodArgumentNotValidExceptionHandler(exception: MethodArgumentNotValidException): ExceptionResponse {
        val errors = exception.bindingResult.allErrors
        val error = errors.firstOrNull { it is FieldError } as FieldError?
        val message = error?.let { "Request is not valid because ${it.field} ${it.defaultMessage}" }
                ?: "Some of request parameters is wrong. Please check request  according do documentation https://docs.openfuture.io/."
        return ExceptionResponse(BAD_REQUEST.value(), message, errors.map { ErrorDto(it) })
    }

    @ResponseStatus(code = BAD_REQUEST)
    @ExceptionHandler(CompileException::class)
    fun compileExceptionHandler(exception: CompileException): ExceptionResponse =
            ExceptionResponse(BAD_REQUEST.value(), exception.message!!)

    @ResponseStatus(code = BAD_REQUEST)
    @ExceptionHandler(ExecuteTransactionException::class)
    fun executeTransactionExceptionHandler(exception: ExecuteTransactionException): ExceptionResponse =
            ExceptionResponse(BAD_REQUEST.value(), exception.message!!)

    @ResponseStatus(code = BAD_REQUEST)
    @ExceptionHandler(FunctionCallException::class)
    fun functionCallExceptionHandler(exception: FunctionCallException): ExceptionResponse =
            ExceptionResponse(BAD_REQUEST.value(), exception.message!!)

    @ResponseStatus(code = BAD_REQUEST)
    @ExceptionHandler(TemplateProcessingException::class)
    fun templateProcessionExceptionHandler(exception: TemplateProcessingException): ExceptionResponse =
            ExceptionResponse(BAD_REQUEST.value(), exception.message!!)

    @ResponseStatus(code = BAD_REQUEST)
    @ExceptionHandler(IllegalStateException::class)
    fun illegalStateExceptionHandler(exception: IllegalStateException): ExceptionResponse =
            ExceptionResponse(BAD_REQUEST.value(), exception.message ?: """Something went wrong. Please read the
                |documentation https://docs.openfuture.io/ or contact us openplatform@zensoft.io""".trimMargin())

    @ResponseStatus(code = BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException::class)
    fun illegalArgumentExceptionHandler(exception: IllegalArgumentException): ExceptionResponse =
            ExceptionResponse(BAD_REQUEST.value(), exception.message ?: """Something went wrong. Please read the
                |documentation https://docs.openfuture.io/ or contact us openplatform@zensoft.io""".trimMargin())

}