package io.openfuture.api.service

import io.openfuture.api.entity.auth.OpenKey
import io.openfuture.api.entity.auth.User
import io.openfuture.api.exception.NotFoundException
import io.openfuture.api.repository.OpenKeyRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * @author Kadach Alexey
 */
@Service
class DefaultOpenKeyService(
        private val repository: OpenKeyRepository
) : OpenKeyService {

    @Transactional(readOnly = true)
    override fun getAllByUser(user: User): List<OpenKey> = repository.findAllByUser(user)

    @Transactional(readOnly = true)
    override fun get(key: String, user: User): OpenKey = repository.findByValueAndUser(key, user)
            ?: throw NotFoundException("Not found key $key")

    @Transactional
    override fun generate(user: User): OpenKey = repository.save(OpenKey(user))

}