package io.zensoft.open.api.service

import io.zensoft.open.api.model.auth.User
import io.zensoft.open.api.repository.UserRepository
import org.springframework.security.oauth2.core.oidc.user.OidcUser
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * @author Kadach Alexey
 */
@Service
class DefaultUserService(
        private val repository: UserRepository,
        private val openKeyService: OpenKeyService
) : UserService {

    @Transactional(readOnly = true)
    override fun findByGoogleId(googleId: String): User? = repository.findByGoogleId(googleId)

    @Transactional
    override fun save(user: OidcUser): User {
        val persistUser = repository.save(User(user))

        openKeyService.save(persistUser)

        return persistUser
    }

}