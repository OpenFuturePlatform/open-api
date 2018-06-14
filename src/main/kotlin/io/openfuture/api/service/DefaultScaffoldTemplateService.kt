package io.openfuture.api.service

import io.openfuture.api.domain.scaffold.SaveScaffoldTemplateRequest
import io.openfuture.api.entity.auth.User
import io.openfuture.api.entity.scaffold.ScaffoldTemplate
import io.openfuture.api.entity.scaffold.ScaffoldTemplateProperty
import io.openfuture.api.exception.NotFoundException
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
    override fun getAll(user: User): List<ScaffoldTemplate> = repository.findAllByUserAndDeletedIsFalse(user)

    @Transactional(readOnly = true)
    override fun get(id: Long, user: User): ScaffoldTemplate = repository.findByIdAndUser(id, user)
            ?: throw NotFoundException("Not found scaffold template with id $id")

    @Transactional
    override fun save(request: SaveScaffoldTemplateRequest, user: User): ScaffoldTemplate {
        val template = repository.save(ScaffoldTemplate.of(request, user))
        val properties = request.properties.map { propertyRepository.save(ScaffoldTemplateProperty.of(template, it)) }
        template.property.addAll(properties)
        return template
    }

    @Transactional
    override fun delete(id: Long, user: User): ScaffoldTemplate {
        val template = get(id, user)

        template.deleted = true

        return repository.save(template)
    }

}
