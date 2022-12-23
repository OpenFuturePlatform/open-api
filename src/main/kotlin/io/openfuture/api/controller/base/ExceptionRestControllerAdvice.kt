package io.openfuture.api.controller.base

import io.openfuture.api.domain.exception.ErrorDto
import io.openfuture.api.domain.exception.ExceptionResponse
import io.openfuture.api.exception.*
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import javax.validation.ConstraintViolationException

@RestControllerAdvice
class ExceptionRestControllerAdvice {

    @ResponseStatus(code = BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun methodArgumentNotValidExceptionHandler(exception: MethodArgumentNotValidException): ExceptionResponse {
        val errors = exception.bindingResult.allErrors
        val error = errors.firstOrNull { it is FieldError } as FieldError?
        val message = error?.let { "Request is not valid because ${it.field} ${it.defaultMessage}" }
                ?: "Some of request parameters are wrong. Please check request according to documentation https://docs.openfuture.io/."
        return ExceptionResponse(BAD_REQUEST.value(), message, errors.map { ErrorDto(it) })
    }

    @ResponseStatus(code = BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun httpMessageNotReadableExceptionHandler(exception: HttpMessageNotReadableException): ExceptionResponse =
            ExceptionResponse(BAD_REQUEST.value(), exception.message!!)


    @ResponseStatus(code = BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException::class)
    fun constraintViolationExceptionHandler(exception: ConstraintViolationException): ExceptionResponse =
            ExceptionResponse(BAD_REQUEST.value(), exception.message!!)


    @ResponseStatus(code = BAD_REQUEST)
    @ExceptionHandler(CompileException::class)
    fun compileExceptionHandler(exception: CompileException): ExceptionResponse =
            ExceptionResponse(BAD_REQUEST.value(), exception.message!!)

    @ResponseStatus(code = BAD_REQUEST)
    @ExceptionHandler(ExecuteTransactionException::class)
    fun executeTransactionExceptionHandler(exception: ExecuteTransactionException): ExceptionResponse =
            ExceptionResponse(BAD_REQUEST.value(), exception.message!!)

    @ResponseStatus(code = BAD_REQUEST)
    @ExceptionHandler(AddressException::class)
    fun addressExceptionHandler(exception: AddressException): ExceptionResponse =
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

    @ResponseStatus(code = NOT_FOUND)
    @ExceptionHandler(NotFoundException::class)
    fun notFoundExceptionHandler(exception: NotFoundException): ExceptionResponse =
        ExceptionResponse(NOT_FOUND.value(), exception.message!!)

    @ResponseStatus(code = BAD_REQUEST)
    @ExceptionHandler(RuntimeException::class)
    fun runtimeExceptionHandler(exception: RuntimeException): ExceptionResponse =
        ExceptionResponse(BAD_REQUEST.value(), exception.message ?: """Something went wrong. Please read the
                |documentation https://docs.openfuture.io/ or contact us openplatform@zensoft.io""".trimMargin())

}