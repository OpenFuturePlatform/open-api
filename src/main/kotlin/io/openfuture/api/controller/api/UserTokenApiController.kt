package io.openfuture.api.controller.api

import io.openfuture.api.annotation.CurrentUser
import io.openfuture.api.domain.PageRequest
import io.openfuture.api.domain.PageResponse
import io.openfuture.api.domain.token.UserTokenDto
import io.openfuture.api.domain.token.UserTokenRequest
import io.openfuture.api.entity.auth.User
import io.openfuture.api.entity.token.UserCustomToken
import io.openfuture.api.service.TokenService
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/api/token")
class UserTokenApiController(
    private val tokenService: TokenService
) {
    @GetMapping
    fun getAllPageable(@CurrentUser user: User, pageRequest: PageRequest): PageResponse<UserTokenDto> {
        val tokens = tokenService.getAll(pageRequest).map { UserTokenDto(it) }
        return PageResponse(tokens)
    }

    @PostMapping
    fun save(@Valid @RequestBody request: UserTokenRequest, @CurrentUser user: User): UserCustomToken =
        tokenService.save(request, user)

    @GetMapping("/list")
    fun getAll(): List<UserTokenDto> {
        return tokenService.getAll().map { UserTokenDto(it) }
    }
}