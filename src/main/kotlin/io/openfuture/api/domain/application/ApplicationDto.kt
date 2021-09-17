package io.openfuture.api.domain.application

import io.openfuture.api.domain.auth.UserDto
import io.openfuture.api.entity.application.Application
import io.openfuture.api.entity.scaffold.Currency

data class ApplicationDto(
    val id: Long,
    val name: String,
    val user: UserDto,
    val defaultCurrency: Currency,
    val active: Boolean,
    val expirationPeriod: Int,
    val webHook: String?
){
    constructor(application: Application) : this(
        application.id,
        application.name,
        UserDto(application.user),
        application.getCurrency()!!,
        application.active,
        application.expirationPeriod!!,
        application.webHook
    )
}
