package io.openfuture.api.controller.api

import io.openfuture.api.annotation.CurrentToken
import io.openfuture.api.annotation.CurrentUser
import io.openfuture.api.domain.auth.OpenKeyDto
import io.openfuture.api.domain.scaffold.GenerateOpenKeyRequest
import io.openfuture.api.entity.auth.User
import io.openfuture.api.service.OpenKeyService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/keys")
class OpenKeyApiController(
        private val service: OpenKeyService
) {

    @GetMapping
    fun getAll(@CurrentUser user: User): List<OpenKeyDto> = service.getAll(user).map { OpenKeyDto(it) }

    @PostMapping
    fun generate(@RequestBody request: GenerateOpenKeyRequest, @CurrentUser user: User): OpenKeyDto {
        val openKey = service.generate(request, user)
        return OpenKeyDto(openKey)
    }

    @DeleteMapping("/{key}")
    fun disable(@PathVariable key: String, @CurrentToken token: String): OpenKeyDto {
        if (token == key) {
            throw IllegalArgumentException("Can't disable used token")
        }
        val openKey = service.disable(key)
        return OpenKeyDto(openKey)
    }

}