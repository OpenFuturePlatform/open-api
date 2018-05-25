package io.zensoft.open.api.domain.auth

import io.zensoft.open.api.model.User

/**
 * @author Kadach Alexey
 */
data class UserDto(
        val id: Long,
        val credits: Int,
        val openKeys: List<OpenKeyDto>
) {

    constructor(user: User): this(user.id , user.credits, user.openKeys.map { OpenKeyDto(it) })

}