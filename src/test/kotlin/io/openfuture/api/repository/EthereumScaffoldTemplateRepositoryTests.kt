package io.openfuture.api.repository

import io.openfuture.api.config.RepositoryTests
import io.openfuture.api.entity.scaffold.EthereumScaffoldTemplate
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

internal class EthereumScaffoldTemplateRepositoryTests : RepositoryTests() {

    @Autowired
    private lateinit var repository: EthereumScaffoldTemplateRepository


    @Test
    fun findAllByUserAndDeletedIsFalseTest() {
        val scaffoldTemplate = createScaffoldTemplate("template")
        val deletedScaffoldTemplate = createScaffoldTemplate("deleted_template").apply { deleted = true }
        entityManager.persist(scaffoldTemplate)
        entityManager.persist(deletedScaffoldTemplate)

        val actualScaffoldTemplates = repository.findAllByDeletedIsFalse()

        assertThat(actualScaffoldTemplates.contains(scaffoldTemplate)).isTrue()
    }

    private fun createScaffoldTemplate(scaffoldTemplateName: String): EthereumScaffoldTemplate = EthereumScaffoldTemplate(scaffoldTemplateName,
            "developerAddress", "description", "fiat_amount", 1, "conversionAmount", "webHook", mutableListOf())

}
