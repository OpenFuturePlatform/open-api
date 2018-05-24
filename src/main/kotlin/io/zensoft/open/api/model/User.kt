package io.zensoft.open.api.model

import io.zensoft.open.api.model.base.BaseModel
import org.springframework.security.oauth2.core.oidc.user.OidcUser
import java.util.*
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table

/**
 * @author Kadach Alexey
 */
@Entity
@Table(name = "users")
class User(

        @Column(name = "google_id", nullable = false, unique = true)
        val googleId: String,

        @Column(name = "public_key", nullable = false, unique = true)
        val publicKey: String = "op_pk_${UUID.randomUUID()}",

        @Column(name = "credits", nullable = false)
        val credits: Int = 0

): BaseModel() {

    constructor(user: OidcUser) : this(user.subject)

}