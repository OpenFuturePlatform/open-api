package io.openfuture.api.service

import io.openfuture.api.domain.scaffold.DeployScaffoldRequest
import io.openfuture.api.model.auth.OpenKey
import io.openfuture.api.model.auth.User
import io.openfuture.api.model.scaffold.Scaffold
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.security.oauth2.core.oidc.user.OidcUser

/**
 * @author Kadach Alexey
 */
interface ScaffoldService {

    fun getAll(user: User, pageRequest: Pageable): Page<Scaffold>

    fun get(address: String): Scaffold

    fun deploy(request: DeployScaffoldRequest, user: User): Scaffold

}

interface OpenKeyService {

    fun get(key: String, user: User): OpenKey

    fun getByUser(user: User): List<OpenKey>

    fun save(user: User): OpenKey

}

interface UserService {

    fun findByGoogleId(googleId: String): User?

    fun save(user: OidcUser): User

}