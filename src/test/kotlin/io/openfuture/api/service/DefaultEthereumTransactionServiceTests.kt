package io.openfuture.api.service

import io.openfuture.api.config.UnitTest
import io.openfuture.api.config.any
import io.openfuture.api.entity.auth.OpenKey
import io.openfuture.api.entity.auth.User
import io.openfuture.api.entity.scaffold.EthereumScaffold
import io.openfuture.api.entity.scaffold.EthereumTransaction
import io.openfuture.api.entity.scaffold.ScaffoldVersion.V1
import io.openfuture.api.repository.EthereumTransactionRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import java.util.*

internal class DefaultEthereumTransactionServiceTests : UnitTest() {

    @Mock private lateinit var pageable: Pageable
    @Mock private lateinit var repository: EthereumTransactionRepository

    private lateinit var service: EthereumTransactionService


    @Before
    fun setUp() {
        service = DefaultEthereumTransactionService(repository)
    }

    @Test
    fun getAllTest() {
        val scaffold = createScaffold()
        val expectedTransactionPages = PageImpl(Collections.singletonList(createTransaction()), pageable, 1)

        given(repository.findAllByEthereumScaffoldOrderByDateDesc(scaffold, pageable)).willReturn(expectedTransactionPages)

        val actualTransactionPages = service.getAll(scaffold, pageable)

        assertThat(actualTransactionPages).isEqualTo(expectedTransactionPages)
    }

    @Test
    fun findShouldReturnTransactionByHash() {
        val expectedTransaction = createTransaction()

        given(repository.findByHashAndIndex(expectedTransaction.hash, expectedTransaction.index)).willReturn(expectedTransaction)

        val actualTransaction = service.find(expectedTransaction.hash, expectedTransaction.index)

        assertThat(actualTransaction).isEqualTo(expectedTransaction)
    }

    @Test
    fun saveTest() {
        val transaction = createTransaction()

        given(repository.save(any(EthereumTransaction::class.java))).will { invocation -> invocation.arguments[0] }

        val actualTransaction = service.save(transaction)

        assertThat(actualTransaction.data).isEqualTo(transaction.data)
        assertThat(actualTransaction.ethereumScaffold).isEqualTo(transaction.ethereumScaffold)
    }

    private fun createTransaction(): EthereumTransaction = EthereumTransaction("hash", "index", createScaffold(), "data binary")

    private fun createScaffold(): EthereumScaffold {
        val openKey = OpenKey(User("104113085667282103363"))

        return EthereumScaffold("address", "abi", "fiatAmount", 1, "conversionAmount", Collections.emptyList(), V1.getId(), openKey, "developerAddress", "description", "webHook").apply { id = 1L }
    }

}