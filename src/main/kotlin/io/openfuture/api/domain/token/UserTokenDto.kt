package io.openfuture.api.domain.token

import io.openfuture.api.entity.token.UserCustomToken


data class UserTokenDto(
    val id: Long,
    val name: String,
    val symbol: String,
    val decimal: Int,
    val address: String
){
    constructor(userCustomToken: UserCustomToken) : this(
        userCustomToken.id,
        userCustomToken.name,
        userCustomToken.symbol,
        userCustomToken.decimal,
        userCustomToken.address
    )
}
