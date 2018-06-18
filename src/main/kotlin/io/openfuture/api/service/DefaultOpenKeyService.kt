package io.openfuture.api.service

import io.openfuture.api.entity.auth.OpenKey
import io.openfuture.api.entity.auth.User
import io.openfuture.api.exception.NotFoundException
import io.openfuture.api.repository.OpenKeyRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class DefaultOpenKeyService(
        private val repository: OpenKeyRepository
) : OpenKeyService {

    @Transactional(readOnly = true)
    override fun getAll(user: User): List<OpenKey> = repository.findAllByUser(user)

    @Transactional(readOnly = true)
    override fun get(key: String): OpenKey = repository.findByValue(key)
            ?: throw NotFoundException("Not found key $key")

    @Transactional(readOnly = true)
    override fun find(key: String): OpenKey? = repository.findByValue(key)

    @Transactional
    override fun generate(user: User): OpenKey = repository.save(OpenKey(user))

}