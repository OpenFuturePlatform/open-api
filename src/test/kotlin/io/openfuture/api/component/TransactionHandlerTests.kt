package io.openfuture.api.component

import io.openfuture.api.config.UnitTest
import io.openfuture.api.config.any
import io.openfuture.api.entity.auth.OpenKey
import io.openfuture.api.entity.auth.User
import io.openfuture.api.entity.scaffold.Scaffold
import io.openfuture.api.entity.scaffold.Transaction
import io.openfuture.api.repository.ScaffoldRepository
import io.openfuture.api.service.TransactionService
import org.junit.Before
import org.junit.Test
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.verify
import org.mockito.Mock
import org.mockito.Mockito.never
import org.web3j.protocol.core.methods.response.Log

internal class TransactionHandlerTests : UnitTest() {

    @Mock private lateinit var transactionService: TransactionService
    @Mock private lateinit var scaffoldRepository: ScaffoldRepository

    private lateinit var transactionHandler: TransactionHandler


    @Before
    fun setUp() {
        transactionHandler = TransactionHandler(transactionService, scaffoldRepository)
    }


    @Test
    fun handle() {
        val addressValue = "0xba37163625b3f2e96112562858c12b75963af138"
        val log = Log().apply { address = addressValue; data = "data" ; type = "type"}
        val user = User("104113085667282103363")
        val scaffold = Scaffold(addressValue, OpenKey(user), "abi", "developerAddress", "description", "fiatAmount", 1,
                "conversionAmount", "https://test.com", mutableListOf(), true)
        val transaction = Transaction.of(scaffold, log)

        given(scaffoldRepository.findByAddress(log.address)).willReturn(scaffold)
        given(transactionService.save(any(Transaction::class.java))).will { invocation -> invocation.arguments[0] }

        transactionHandler.handle(log)
    }

    @Test
    fun handleWithEmptyScaffold() {
        val log = Log().apply { address = "0xba37163625b3f2e96112562858c12b75963af138" }

        given(scaffoldRepository.findByAddress(log.address)).willReturn(null)

        transactionHandler.handle(log)

        verify(transactionService, never()).save(any(Transaction::class.java))
    }

}