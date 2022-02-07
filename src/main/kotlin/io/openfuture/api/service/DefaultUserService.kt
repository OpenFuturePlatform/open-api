package io.openfuture.api.service

import io.openfuture.api.entity.auth.User
import io.openfuture.api.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class DefaultUserService(
        private val repository: UserRepository
) : UserService {

    @Transactional(readOnly = true)
    override fun findByGoogleId(googleId: String): User? = repository.findByGoogleId(googleId)

    @Transactional
    override fun save(user: User): User {
        return repository.save(user)
    }

}