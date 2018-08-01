package io.openfuture.api.component.web3

import io.openfuture.api.component.web3.event.ProcessorEventDecoder
import io.openfuture.api.config.UnitTest
import io.openfuture.api.config.any
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
    @Mock private lateinit var log: Log

    private lateinit var transactionHandler: TransactionHandler


    @Before
    fun setUp() {
        transactionHandler = TransactionHandler(transactionService, scaffoldRepository, eventDecoder, publisher)
        given(log.address).willReturn("0xba37163625b3f2e96112562858c12b75963af138")
    }

    @Test
    fun handleTest() {
        given(transactionService.save(any(Transaction::class.java))).will { invocation -> invocation.arguments[0] }

        transactionHandler.handle(log)
    }

    @Test
    fun handleWhenEmptyScaffoldShouldNotSaveTransactionTest() {
        transactionHandler.handle(log)

        verify(transactionService, never()).save(any(Transaction::class.java))
    }

}