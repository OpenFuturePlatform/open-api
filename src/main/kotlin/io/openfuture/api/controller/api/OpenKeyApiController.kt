package io.openfuture.api.controller.api

import io.openfuture.api.annotation.CurrentUser
import io.openfuture.api.domain.auth.OpenKeyDto
import io.openfuture.api.entity.auth.User
import io.openfuture.api.service.OpenKeyService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/keys")
class OpenKeyApiController(
        private val service: OpenKeyService
) {

    @PostMapping
    fun generateToken(@CurrentUser user: User): OpenKeyDto {
        val key = service.generate(user)
        return OpenKeyDto(key)
    }

}