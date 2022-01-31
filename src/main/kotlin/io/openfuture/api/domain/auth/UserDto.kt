package io.openfuture.api.domain.auth

import io.openfuture.api.entity.auth.User

data class UserDto(
    val id: Long,
    val credits: Int,
    val roles: List<RoleDto>
) {

    constructor(user: User) : this(
        user.id,
        user.credits,
        user.roles.map { RoleDto(it) }
    )

}