package io.openfuture.api.domain.application

import io.openfuture.api.domain.auth.UserDto
import io.openfuture.api.entity.application.Application
import io.openfuture.api.entity.scaffold.Currency

data class ApplicationDto(
    val id: Long,
    val name: String,
    val user: UserDto,
    val active: Boolean,
    val webHook: String?,
    val apiAccessKey: String?,
    val apiSecretKey: String?
){
    constructor(application: Application) : this(
        application.id,
        application.name,
        UserDto(application.user),
        application.active,
        application.webHook,
        application.apiAccessKey,
        application.apiSecretKey
    )
}
