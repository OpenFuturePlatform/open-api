package io.openfuture.api.component.web3

import io.openfuture.api.component.web3.event.ProcessorEventDecoder
import io.openfuture.api.config.UnitTest
import io.openfuture.api.config.any
import io.openfuture.api.entity.auth.OpenKey
import io.openfuture.api.entity.auth.User
import io.openfuture.api.entity.scaffold.Scaffold
import io.openfuture.api.entity.scaffold.ScaffoldVersion.V1
import io.openfuture.api.entity.scaffold.Transaction
import io.openfuture.api.repository.ScaffoldRepository
import io.openfuture.api.service.TransactionService
import org.junit.Before
import org.junit.Test
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.verify
import org.mockito.Mock
import org.mockito.Mockito.never
import org.springframework.context.ApplicationEventPublisher
import org.web3j.protocol.core.methods.response.Log

internal class TransactionHandlerTests : UnitTest() {

    @Mock private lateinit var transactionService: TransactionService
    @Mock private lateinit var scaffoldRepository: ScaffoldRepository
    @Mock private lateinit var eventDecoder: ProcessorEventDecoder
    @Mock private lateinit var publisher: ApplicationEventPublisher

    private lateinit var transactionHandler: TransactionHandler


    @Before
    fun setUp() {
        transactionHandler = TransactionHandler(transactionService, scaffoldRepository, eventDecoder, publisher)
    }

    @Test
    fun handleTest() {
        val log = createLog()
        val user = User("104113085667282103363")
        val scaffold = Scaffold("0xba37163625b32e96112562858c12b75963af138", OpenKey(user), "abi", "developerAddress",
                "description", "fiatAmount", 1, "conversionAmount", V1.getId(), "https://test.com", mutableListOf())

        given(scaffoldRepository.findByAddressIgnoreCase(log.address)).willReturn(scaffold)
        given(transactionService.save(any(Transaction::class.java))).will { invocation -> invocation.arguments[0] }

        transactionHandler.handle(log)
    }

    @Test
    fun handleWhenEmptyScaffoldShouldNotSaveTransactionTest() {
        val log = createLog()

        given(scaffoldRepository.findByAddressIgnoreCase(log.address)).willReturn(null)

        transactionHandler.handle(log)

        verify(transactionService, never()).save(any(Transaction::class.java))
    }

    private fun createLog(): Log = Log().apply { transactionHash = "hash";
        address = "0xba37163625b3f2e96112562858c12b75963af138"; data = "data" ; type = "type"}
}