package io.openfuture.api.domain.auth

data class CurrentUserDto(
        val user: UserDto,
        val token: String
)