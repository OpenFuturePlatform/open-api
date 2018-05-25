package io.zensoft.open.api.service

import io.zensoft.open.api.model.OpenKey
import io.zensoft.open.api.model.Scaffold
import io.zensoft.open.api.model.User
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.security.oauth2.core.oidc.user.OidcUser

/**
 * @author Kadach Alexey
 */
interface ScaffoldService {

    fun getAll(user: User, pageRequest: Pageable): Page<Scaffold>

    fun get(address: String): Scaffold

}

interface OpenKeyService {

    fun get(key: String): OpenKey

    fun getByUser(user: User): List<OpenKey>

    fun save(user: User): OpenKey

}

interface UserService {

    fun findByGoogleId(googleId: String): User?

    fun save(user: OidcUser): User

}