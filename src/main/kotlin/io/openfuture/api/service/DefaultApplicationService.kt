package io.openfuture.api.service

import io.openfuture.api.component.keyGenerator.DigitalKeyGenerator
import io.openfuture.api.domain.application.ApplicationRequest
import io.openfuture.api.entity.application.Application
import io.openfuture.api.entity.auth.User
import io.openfuture.api.exception.NotFoundException
import io.openfuture.api.repository.ApplicationRepository
import io.openfuture.api.util.KeyGeneratorUtils
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
@Transactional
class DefaultApplicationService(
    private val applicationRepository: ApplicationRepository,
    private val digitalKeyGenerator: DigitalKeyGenerator
): ApplicationService {

    override fun getAll(user: User, pageRequest: Pageable): Page<Application> =
        applicationRepository.findAllByUser(user, pageRequest)

    override fun getById(id: Long): Application {
        return applicationRepository.findById(id).orElseThrow {  throw NotFoundException("Not found application with id : $id")}
    }

    override fun save(request: ApplicationRequest, user: User): Application {
        val applicationAccessKey = digitalKeyGenerator.generateApplicationAccessKey()
        return applicationRepository.save(Application.of(request, user, applicationAccessKey))
    }

    override fun delete(id: Long) {
        applicationRepository.deleteById(id)
    }
}