package io.openfuture.api.entity.application

import io.openfuture.api.domain.application.ApplicationAccessKey
import io.openfuture.api.domain.application.ApplicationRequest
import io.openfuture.api.entity.auth.User
import io.openfuture.api.entity.base.BaseModel
import io.openfuture.api.util.KeyGeneratorUtils
import javax.persistence.*

@Entity
@Table(name = "user_applications")
class Application(

    @Column(name = "name", nullable = false, unique = true)
    val name: String,

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,

    @Column(name = "web_hook")
    val webHook: String?,

    @Column(name = "active")
    var active: Boolean = true,

    @Column(name = "api_access_key")
    var apiAccessKey: String,

    @Column(name = "api_secret_key")
    var apiSecretKey: String

):BaseModel() {

    companion object {

        fun of(request: ApplicationRequest, user: User, applicationAccessKey: ApplicationAccessKey): Application {

            return Application(
                request.name!!,
                user,
                request.webHook,
                request.active,
                applicationAccessKey.accessKey,
                applicationAccessKey.secretKey
            )
        }
    }
}