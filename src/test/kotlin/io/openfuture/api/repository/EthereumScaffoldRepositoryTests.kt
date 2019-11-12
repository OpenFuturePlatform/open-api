package io.openfuture.api.repository

import io.openfuture.api.config.RepositoryTests
import io.openfuture.api.domain.PageRequest
import io.openfuture.api.entity.auth.OpenKey
import io.openfuture.api.entity.auth.User
import io.openfuture.api.entity.scaffold.EthereumScaffold
import io.openfuture.api.entity.scaffold.ScaffoldVersion.V1
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import java.util.*

internal class EthereumScaffoldRepositoryTests : RepositoryTests() {

    @Autowired
    private lateinit var repositoryEthereum: EthereumScaffoldRepository


    @Test
    fun findByAddressAndOpenKeyUserTest() {
        val expectedScaffold = persistEntities()
        val address = expectedScaffold.address
        val user = expectedScaffold.openKey.user

        val actualScaffold = repositoryEthereum.findByAddressAndOpenKeyUser(address, user)

        assertThat(actualScaffold).isEqualTo(expectedScaffold)
    }

    @Test
    fun findByAddressTest() {
        val expectedScaffold = persistEntities()
        val address = expectedScaffold.address

        val actualScaffold = repositoryEthereum.findByAddressIgnoreCase(address)

        assertThat(actualScaffold).isEqualTo(expectedScaffold)
    }

    @Test
    fun findAllByOpenKeyUserTest() {
        val expectedScaffold = persistEntities()
        val user = expectedScaffold.openKey.user

        val actualScaffolds = repositoryEthereum.findAllByOpenKeyUserOrderByIdDesc(user, PageRequest())

        assertThat(actualScaffolds.contains(expectedScaffold)).isTrue()
    }

    private fun persistEntities(): EthereumScaffold {
        val user = User("googleId")
        entityManager.persist(user)

        val openKey = OpenKey(user, Date(), "op_pk_9de7cbb4-857c-49e9-87d2-fc91428c4c12")
        entityManager.persist(openKey)

        val scaffold = EthereumScaffold("address", "abi", "fiatAmount", 1, "conversionAmount", Collections.emptyList(), V1.getId(), openKey, "developerAddress", "description", "webHook")
        entityManager.persist(scaffold)

        return scaffold
    }

}