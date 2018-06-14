package io.openfuture.api.service

import io.openfuture.api.domain.scaffold.SaveScaffoldTemplateRequest
import io.openfuture.api.entity.auth.User
import io.openfuture.api.entity.scaffold.ScaffoldTemplate
import io.openfuture.api.entity.scaffold.ScaffoldTemplateProperty
import io.openfuture.api.repository.ScaffoldTemplatePropertyRepository
import io.openfuture.api.repository.ScaffoldTemplateRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * @author Kadach Alexey
 */
@Service
class DefaultScaffoldTemplateService(
        private val repository: ScaffoldTemplateRepository,
        private val propertyRepository: ScaffoldTemplatePropertyRepository
) : ScaffoldTemplateService {

    @Transactional(readOnly = true)
    override fun getAll(user: User): List<ScaffoldTemplate> = repository.findAllByUser(user)

    @Transactional
    override fun save(request: SaveScaffoldTemplateRequest, user: User): ScaffoldTemplate {
        val scaffoldTemplate = repository.save(ScaffoldTemplate.of(request, user))
        val properties = request.properties.map { propertyRepository.save(ScaffoldTemplateProperty.of(scaffoldTemplate, it)) }
        scaffoldTemplate.property.addAll(properties)
        return scaffoldTemplate
    }

}