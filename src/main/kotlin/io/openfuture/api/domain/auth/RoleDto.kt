package io.openfuture.api.domain.auth

import io.openfuture.api.entity.auth.Role

data class RoleDto(
        val key: String
) {

    constructor(role: Role) : this(role.key)

}