package io.zensoft.open.api.domain.auth

import io.zensoft.open.api.model.User

/**
 * @author Kadach Alexey
 */
data class UserDto(
        val publicKey: String,
        val credits: Int
) {

    constructor(user: User): this(user.publicKey, user.credits)

}