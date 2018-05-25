package io.zensoft.open.api.service

import io.zensoft.open.api.exception.NotFoundException
import io.zensoft.open.api.model.OpenKey
import io.zensoft.open.api.model.User
import io.zensoft.open.api.repository.OpenKeyRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * @author Kadach Alexey
 */
@Service
class DefaultOpenKeyService(
        private val repository: OpenKeyRepository
): OpenKeyService {

    @Transactional(readOnly = true)
    override fun get(key: String): OpenKey = repository.findByValue(key)
            ?: throw NotFoundException("Not found key $key")

    @Transactional(readOnly = true)
    override fun getByUser(user: User): List<OpenKey> = repository.findAllByUser(user)

    @Transactional
    override fun save(user: User): OpenKey = repository.save(OpenKey(user))

}