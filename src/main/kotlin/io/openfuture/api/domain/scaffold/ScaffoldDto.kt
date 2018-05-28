package io.openfuture.api.domain.scaffold

import io.openfuture.api.domain.auth.UserDto
import io.openfuture.api.model.scaffold.Scaffold

/**
 * @author Kadach Alexey
 */
data class ScaffoldDto(
        val address: String,
        val user: UserDto,
        val abi: String,
        val enabled: Boolean
) {

    constructor(scaffold: Scaffold): this(scaffold.address, UserDto(scaffold.user), scaffold.abi, scaffold.enabled)

}