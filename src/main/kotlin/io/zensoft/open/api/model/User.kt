package io.zensoft.open.api.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.security.oauth2.core.oidc.user.OidcUser
import java.util.*

/**
 * @author Kadach Alexey
 */
@Document(collection = "users")
class User(
        @Id val googleId: String,
        val openPublicKey: String = "op_pk_${UUID.randomUUID()}",
        val credits: Int = 0
) {

    constructor(user: OidcUser) : this(user.subject)

}