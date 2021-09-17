package io.openfuture.api.service

import io.openfuture.api.domain.application.ApplicationRequest
import io.openfuture.api.entity.application.Application
import io.openfuture.api.entity.auth.User
import io.openfuture.api.repository.ApplicationRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class DefaultApplicationService(
    private val applicationRepository: ApplicationRepository
): ApplicationService {

    override fun getAll(user: User, pageRequest: Pageable): Page<Application> =
        applicationRepository.findAllByUser(user, pageRequest)

    override fun save(request: ApplicationRequest, user: User): Application {
        return applicationRepository.save(Application.of(request, user))
    }

    override fun delete(id: Long) {
        applicationRepository.deleteById(id)
    }
}