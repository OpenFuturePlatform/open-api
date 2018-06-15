package io.openfuture.api.repository

import io.openfuture.api.config.RepositoryTests
import io.openfuture.api.entity.auth.User
import io.openfuture.api.entity.scaffold.ScaffoldTemplate
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

/**
 * @author Alexey Skadorva
 */
internal class ScaffoldTemplateRepositoryTests : RepositoryTests() {

    @Autowired private lateinit var repository: ScaffoldTemplateRepository


    @Test
    fun findAllByUserAndDeletedIsFalse() {
        val user = User("googleId")
        entityManager.persist(user)

        val expectedScaffoldTemplate = ScaffoldTemplate("name", user, "developerAddress", "description", "fiat_amount",
                1, "conversionAmount", "webHook")
        val savedId = entityManager.persistAndGetId(expectedScaffoldTemplate) as Long

        val actualScaffoldTemplate = repository.findByIdAndUser(savedId, user)

        assertThat(actualScaffoldTemplate).isEqualTo(expectedScaffoldTemplate)
    }

}
