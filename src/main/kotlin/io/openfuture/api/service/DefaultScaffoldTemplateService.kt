package io.openfuture.api.service

import io.openfuture.api.domain.scaffold.SaveScaffoldTemplateRequest
import io.openfuture.api.entity.scaffold.ScaffoldTemplate
import io.openfuture.api.entity.scaffold.ScaffoldTemplateProperty
import io.openfuture.api.exception.NotFoundException
import io.openfuture.api.repository.ScaffoldTemplatePropertyRepository
import io.openfuture.api.repository.ScaffoldTemplateRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class DefaultScaffoldTemplateService(
        private val repository: ScaffoldTemplateRepository,
        private val propertyRepository: ScaffoldTemplatePropertyRepository
) : ScaffoldTemplateService {

    @Transactional(readOnly = true)
    override fun getAll(): List<ScaffoldTemplate> = repository.findAllByDeletedIsFalse()

    @Transactional(readOnly = true)
    override fun get(id: Long): ScaffoldTemplate = repository.findById(id)
            .orElseThrow { throw NotFoundException("Not found scaffold template with id $id") }

    @Transactional
    override fun save(request: SaveScaffoldTemplateRequest): ScaffoldTemplate {
        val template = repository.save(ScaffoldTemplate.of(request))
        val properties = request.properties.map { propertyRepository.save(ScaffoldTemplateProperty.of(template, it)) }
        template.property.addAll(properties)
        return template
    }

    @Transactional
    override fun delete(id: Long): ScaffoldTemplate {
        val template = get(id)

        template.deleted = true

        return repository.save(template)
    }

}