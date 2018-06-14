package io.openfuture.api.service

import io.openfuture.api.GOOGLE_ID
import io.openfuture.api.ID
import io.openfuture.api.UnitTest
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

/**
 * @author Alexey Skadorva
 */
internal class DefaultTransactionServiceTest : UnitTest() {

    @Mock private lateinit var pageable: Pageable
    @Mock private lateinit var repository: TransactionRepository

    private lateinit var service: TransactionService


    @Before
    fun setUp() {
        service = DefaultTransactionService(repository)
    }

    @Test
    fun getAll() {
        val scaffold = getScaffold()
        val expectedTransactionPages = PageImpl(Collections.singletonList(getTransaction()), pageable, 1)

        given(repository.findAllByScaffold(scaffold, pageable)).willReturn(expectedTransactionPages)

        val actualTransactionPages = service.getAll(scaffold, pageable)

        assertThat(actualTransactionPages).isEqualTo(expectedTransactionPages)
    }

    @Test
    fun save() {
        val transaction = getTransaction()

        given(repository.save(transaction)).willReturn(transaction.apply { id = ID })

        val actualTransaction = service.save(transaction)

        assertThat(actualTransaction).isEqualTo(transaction)

    }

    private fun getTransaction(): Transaction = Transaction(getScaffold(), "data binary", "type")

    private fun getScaffold(): Scaffold {
        val openKey = OpenKey(User(GOOGLE_ID))

        return Scaffold("address", openKey, "abi", "developerAddress", "description", "fiatAmount", 1,
                "conversionAmount", "webHook", Collections.emptyList(), true)
    }

}