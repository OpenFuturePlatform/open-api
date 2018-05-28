package io.openfuture.api.model.auth

import io.openfuture.api.model.base.BaseModel
import org.springframework.security.oauth2.core.oidc.user.OidcUser
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.OneToMany
import javax.persistence.Table

/**
 * @author Kadach Alexey
 */
@Entity
@Table(name = "users")
class User(

        @Column(name = "google_id", nullable = false, unique = true)
        val googleId: String,

        @Column(name = "credits", nullable = false)
        val credits: Int = 0,

        @OneToMany(mappedBy = "user")
        val openKeys: MutableSet<OpenKey> = mutableSetOf()

): BaseModel() {

    constructor(user: OidcUser) : this(user.subject)

}