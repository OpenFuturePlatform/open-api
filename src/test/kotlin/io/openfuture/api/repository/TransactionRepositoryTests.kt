package io.openfuture.api.repository

import io.openfuture.api.config.RepositoryTests
import io.openfuture.api.domain.PageRequest
import io.openfuture.api.entity.auth.OpenKey
import io.openfuture.api.entity.auth.User
import io.openfuture.api.entity.scaffold.EthereumScaffold
import io.openfuture.api.entity.scaffold.EthereumTransaction
import io.openfuture.api.entity.scaffold.ScaffoldVersion.V1
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import java.util.*

internal class TransactionRepositoryTests : RepositoryTests() {

    @Autowired
    private lateinit var repository: EthereumTransactionRepository


    @Test
    fun findAllByScaffoldTest() {
        val user = User("googleId")
        entityManager.persist(user)

        val expectedOpenKey = OpenKey(user, Date(), "op_pk_9de7cbb4-857c-49e9-87d2-fc91428c4c12")
        entityManager.persist(expectedOpenKey)

        val scaffold = EthereumScaffold("address", "abi", "fiatAmount", 1, "conversionAmount", Collections.emptyList(), V1.getId(), expectedOpenKey, "developerAddress", "description", "webHook")
        entityManager.persist(scaffold)

        val transaction = EthereumTransaction("hash", "index", scaffold, "data binary")
        entityManager.persist(transaction)

        val actualOpenKeys = repository.findAllByEthereumScaffoldOrderByDateDesc(scaffold, PageRequest())

        assertThat(actualOpenKeys.contains(transaction)).isTrue()
    }

}
