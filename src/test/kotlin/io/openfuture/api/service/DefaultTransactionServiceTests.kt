package io.openfuture.api.service

import io.openfuture.api.config.UnitTest
import io.openfuture.api.config.any
import io.openfuture.api.entity.auth.OpenKey
import io.openfuture.api.entity.auth.User
import io.openfuture.api.entity.scaffold.Scaffold
import io.openfuture.api.entity.scaffold.Transaction
import io.openfuture.api.repository.TransactionRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import java.util.*

internal class DefaultTransactionServiceTests : UnitTest() {

    @Mock private lateinit var pageable: Pageable
    @Mock private lateinit var repository: TransactionRepository

    private lateinit var service: TransactionService


    @Before
    fun setUp() {
        service = DefaultTransactionService(repository)
    }

    @Test
    fun getAllTest() {
        val scaffold = createScaffold()
        val expectedTransactionPages = PageImpl(Collections.singletonList(createTransaction()), pageable, 1)

        given(repository.findAllByScaffold(scaffold, pageable)).willReturn(expectedTransactionPages)

        val actualTransactionPages = service.getAll(scaffold, pageable)

        assertThat(actualTransactionPages).isEqualTo(expectedTransactionPages)
    }

    @Test
    fun saveTest() {
        val transaction = createTransaction()

        given(repository.save(any(Transaction::class.java))).will { invocation -> invocation.arguments[0] }

        val actualTransaction = service.save(transaction)

        assertThat(actualTransaction.data).isEqualTo(transaction.data)
        assertThat(actualTransaction.scaffold).isEqualTo(transaction.scaffold)
    }

    private fun createTransaction(): Transaction = Transaction(createScaffold(), "data binary")

    private fun createScaffold(): Scaffold {
        val openKey = OpenKey(User("104113085667282103363"))

        return Scaffold("address", openKey, "abi", "developerAddress", "description", "fiatAmount", 1,
                "conversionAmount", "webHook", Collections.emptyList()).apply { id = 1L }
    }

}