package io.openfuture.api.repository

import io.openfuture.api.config.RepositoryTests
import io.openfuture.api.domain.PageRequest
import io.openfuture.api.entity.auth.OpenKey
import io.openfuture.api.entity.auth.User
import io.openfuture.api.entity.scaffold.Scaffold
import io.openfuture.api.entity.scaffold.Transaction
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import java.util.*

internal class TransactionRepositoryTests : RepositoryTests() {

    @Autowired private lateinit var repository: TransactionRepository


    @Test
    fun findAllByScaffold() {
        val user = User("googleId")
        entityManager.persist(user)

        val expectedOpenKey = OpenKey(user, Date(), "op_pk_9de7cbb4-857c-49e9-87d2-fc91428c4c12")
        entityManager.persist(expectedOpenKey)

        val scaffold = Scaffold("address", expectedOpenKey, "abi", "developerAddress", "description", "fiatAmount", 1,
                "conversionAmount", "webHook", Collections.emptyList(), true)
        entityManager.persist(scaffold)

        val transaction = Transaction(scaffold, "data binary", "type")
        entityManager.persist(transaction)

        val actualOpenKey = repository.findAllByScaffold(scaffold, PageRequest())

        assertThat(actualOpenKey.content[0]).isEqualTo(transaction)
    }

}
