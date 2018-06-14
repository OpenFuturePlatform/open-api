package io.openfuture.api.controller.api

import io.openfuture.api.annotation.CurrentUser
import io.openfuture.api.domain.auth.OpenKeyDto
import io.openfuture.api.domain.scaffold.GenerateOpenKeyRequest
import io.openfuture.api.entity.auth.User
import io.openfuture.api.service.OpenKeyService
import org.springframework.web.bind.annotation.*

/**
 * @author Kadach Alexey
 */
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
    fun disable(@PathVariable key: String): OpenKeyDto {
        val openKey = service.disable(key)
        return OpenKeyDto(openKey)
    }

}