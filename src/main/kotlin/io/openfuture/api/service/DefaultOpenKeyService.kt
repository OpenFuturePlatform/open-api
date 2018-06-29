package io.openfuture.api.service

import io.openfuture.api.domain.scaffold.GenerateOpenKeyRequest
import io.openfuture.api.entity.auth.OpenKey
import io.openfuture.api.entity.auth.User
import io.openfuture.api.exception.NotFoundException
import io.openfuture.api.repository.OpenKeyRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class DefaultOpenKeyService(
        private val repository: OpenKeyRepository
) : OpenKeyService {

    @Transactional(readOnly = true)
    override fun getAll(user: User): List<OpenKey> = repository.findAllByUser(user)

    @Transactional(readOnly = true)
    override fun get(key: String): OpenKey = find(key)
            ?: throw NotFoundException("Not found key $key")

    @Transactional(readOnly = true)
    override fun find(key: String): OpenKey? {
        val openKey = repository.findByValueAndEnabledIsTrue(key)
        if (null != openKey?.expiredDate && Date().after(openKey.expiredDate)) {
            return null
        }

        return openKey
    }

    @Transactional
    override fun generate(request: GenerateOpenKeyRequest, user: User): OpenKey =
            repository.save(OpenKey.of(request, user))

    @Transactional
    override fun generate(user: User): OpenKey = generate(GenerateOpenKeyRequest(), user)

    @Transactional
    override fun disable(key: String): OpenKey {
        val openKey = get(key)

        openKey.enabled = false

        return repository.save(openKey)
    }

}