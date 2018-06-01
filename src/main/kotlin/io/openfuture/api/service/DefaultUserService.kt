package io.openfuture.api.service

import io.openfuture.api.entity.auth.User
import io.openfuture.api.repository.UserRepository
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
    override fun save(user: User): User {
        val persistUser = repository.save(user)

        openKeyService.generate(persistUser)

        return persistUser
    }

}