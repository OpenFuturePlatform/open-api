package io.openfuture.api.repository

import io.openfuture.api.config.RepositoryTests
import io.openfuture.api.domain.PageRequest
import io.openfuture.api.entity.auth.OpenKey
import io.openfuture.api.entity.auth.User
import io.openfuture.api.entity.scaffold.Scaffold
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import java.util.*

internal class ScaffoldRepositoryTests : RepositoryTests() {

    @Autowired
    private lateinit var repository: ScaffoldRepository


    @Test
    fun findByAddressAndOpenKeyUser() {
        val expectedScaffold = persistEntities()
        val address = expectedScaffold.address
        val user = expectedScaffold.openKey.user

        val actualScaffold = repository.findByAddressAndOpenKeyUser(address, user)

        assertThat(actualScaffold).isEqualTo(expectedScaffold)
    }

    @Test
    fun findByAddress() {
        val expectedScaffold = persistEntities()
        val address = expectedScaffold.address

        val actualScaffold = repository.findByAddress(address)

        assertThat(actualScaffold).isEqualTo(expectedScaffold)
    }

    @Test
    fun findAllByOpenKeyUser() {
        val expectedScaffold = persistEntities()
        val user = expectedScaffold.openKey.user

        val actualScaffolds = repository.findAllByOpenKeyUser(user, PageRequest())

        assertThat(actualScaffolds.contains(expectedScaffold)).isTrue()
    }


    private fun persistEntities(): Scaffold {
        val user = User("googleId")
        entityManager.persist(user)

        val openKey = OpenKey(user, Date(), "op_pk_9de7cbb4-857c-49e9-87d2-fc91428c4c12")
        entityManager.persist(openKey)

        val scaffold = Scaffold("address", openKey, "abi", "developerAddress", "description", "fiatAmount", 1,
                "conversionAmount", "webHook", Collections.emptyList())
        entityManager.persist(scaffold)

        return scaffold
    }

}