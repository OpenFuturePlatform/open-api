package io.openfuture.api.service

import io.openfuture.api.domain.scaffold.SaveEthereumScaffoldTemplateRequest
import io.openfuture.api.entity.scaffold.EthereumScaffoldTemplate
import io.openfuture.api.entity.scaffold.EthereumScaffoldTemplateProperty
import io.openfuture.api.exception.NotFoundException
import io.openfuture.api.repository.EthereumScaffoldTemplateRepository
import io.openfuture.api.repository.ScaffoldTemplatePropertyRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class DefaultEthereumScaffoldTemplateService(
        private val repository: EthereumScaffoldTemplateRepository,
        private val propertyRepository: ScaffoldTemplatePropertyRepository
) : EthereumScaffoldTemplateService {

    @Transactional(readOnly = true)
    override fun getAll(): List<EthereumScaffoldTemplate> = repository.findAllByDeletedIsFalse()

    @Transactional(readOnly = true)
    override fun get(id: Long): EthereumScaffoldTemplate = repository.findById(id)
            .orElseThrow { throw NotFoundException("Not found scaffold template with id $id") }

    @Transactional
    override fun save(request: SaveEthereumScaffoldTemplateRequest): EthereumScaffoldTemplate {
        val template = repository.save(EthereumScaffoldTemplate.of(request))
        val properties = request.properties.map { propertyRepository.save(EthereumScaffoldTemplateProperty.of(template, it)) }
        template.property.addAll(properties)
        return template
    }

    @Transactional
    override fun delete(id: Long): EthereumScaffoldTemplate {
        val template = get(id)

        template.deleted = true

        return repository.save(template)
    }

}