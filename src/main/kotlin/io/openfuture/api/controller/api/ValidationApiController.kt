package io.openfuture.api.controller.api

import io.openfuture.api.domain.validation.ValidateUrlRequest
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@RequestMapping("/api/validation")
class ValidationApiController {

    @PostMapping("/url")
    fun validateUrl(@Valid @RequestBody request: ValidateUrlRequest): ValidateUrlRequest = request

}