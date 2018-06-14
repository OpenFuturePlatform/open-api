package io.openfuture.api.component

import io.openfuture.api.ADDRESS_VALUE
import io.openfuture.api.GOOGLE_ID
import io.openfuture.api.ID
import io.openfuture.api.UnitTest
import io.openfuture.api.config.any
import io.openfuture.api.entity.auth.OpenKey
import io.openfuture.api.entity.auth.User
import io.openfuture.api.entity.scaffold.Scaffold
import io.openfuture.api.entity.scaffold.Transaction
import io.openfuture.api.repository.ScaffoldRepository
import io.openfuture.api.service.*
import org.junit.Before
import org.junit.Test
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.verify
import org.mockito.Mock
import org.mockito.Mockito.never
import org.web3j.protocol.core.methods.response.Log

/**
 * @author Alexey Skadorva
 */
class TransactionHandlerTest : UnitTest() {

    @Mock private lateinit var transactionService: TransactionService
    @Mock private lateinit var scaffoldRepository: ScaffoldRepository

    private lateinit var transactionHandler: TransactionHandler


    @Before
    fun setUp() {
        transactionHandler = TransactionHandler(transactionService, scaffoldRepository)
    }


    @Test
    fun handle() {
        val log = Log().apply { address = ADDRESS_VALUE; data = "data" ; type = "type"}
        val scaffold = Scaffold(ADDRESS_VALUE, OpenKey(User(GOOGLE_ID)), "abi", "developerAddress", "description", "fiatAmount", 1,
                "conversionAmount", "https://test.com", mutableListOf(), true)
        val transaction = Transaction.of(scaffold, log)

        given(scaffoldRepository.findByAddress(log.address)).willReturn(scaffold)
        given(transactionService.save(any(Transaction::class.java))).willReturn(transaction.apply { id = ID })

        transactionHandler.handle(log)
    }

    @Test
    fun handleWitEmptyScaffold() {
        val log = Log().apply { address = ADDRESS_VALUE }

        given(scaffoldRepository.findByAddress(log.address)).willReturn(null)

        transactionHandler.handle(log)

        verify(transactionService, never()).save(any(Transaction::class.java))
    }

}