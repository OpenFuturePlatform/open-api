package io.zensoft.open.api.controller.base

import io.zensoft.open.api.domain.exception.ErrorDto
import io.zensoft.open.api.domain.exception.ExceptionResponse
import io.zensoft.open.api.exception.CompileException
import io.zensoft.open.api.exception.DeployException
import io.zensoft.open.api.exception.TemplateProcessingException
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.HttpStatus.CONFLICT
import org.springframework.validation.BindException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

/**
 * @author Kadach Alexey
 */
@RestControllerAdvice
class ExceptionRestControllerAdvice {

    @ResponseStatus(code = BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun methodArgumentNotValidExceptionHandler(exception: MethodArgumentNotValidException): ExceptionResponse =
            ExceptionResponse(BAD_REQUEST.value(), BAD_REQUEST.reasonPhrase, exception.bindingResult.allErrors.map { ErrorDto(it) })

    @ResponseStatus(code = BAD_REQUEST)
    @ExceptionHandler(BindException::class)
    fun bindExceptionHandler(exception: BindException): ExceptionResponse =
            ExceptionResponse(BAD_REQUEST.value(), BAD_REQUEST.reasonPhrase, exception.bindingResult.allErrors.map { ErrorDto(it) })

    @ResponseStatus(code = CONFLICT)
    @ExceptionHandler(DataIntegrityViolationException::class)
    fun dataIntegrityViolationExceptionHandler(exception: DataIntegrityViolationException): ExceptionResponse =
            ExceptionResponse(CONFLICT.value(), "Data Integrity Violation")

    @ResponseStatus(code = BAD_REQUEST)
    @ExceptionHandler(CompileException::class)
    fun compileExceptionHandler(exception: CompileException): ExceptionResponse =
            ExceptionResponse(BAD_REQUEST.value(), "Compile Exception")

    @ResponseStatus(code = BAD_REQUEST)
    @ExceptionHandler(DeployException::class)
    fun deployExceptionHandler(exception: DeployException): ExceptionResponse =
            ExceptionResponse(BAD_REQUEST.value(), "Deploy Exception")

    @ResponseStatus(code = BAD_REQUEST)
    @ExceptionHandler(TemplateProcessingException::class)
    fun templateProcessionExceptionHandler(exception: TemplateProcessingException): ExceptionResponse =
            ExceptionResponse(BAD_REQUEST.value(), "Template Processing Exception")


}