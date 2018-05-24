package io.zensoft.open.api.domain.scaffold

import io.zensoft.open.api.domain.auth.UserDto
import io.zensoft.open.api.model.Scaffold

/**
 * @author Kadach Alexey
 */
data class ScaffoldDto(
        val address: String,
        val user: UserDto,
        val abi: String
) {

    constructor(scaffold: Scaffold): this(scaffold.address, UserDto(scaffold.user), scaffold.abi)

}