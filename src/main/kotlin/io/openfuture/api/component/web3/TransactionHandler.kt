package io.openfuture.api.component.web3

import io.openfuture.api.component.event.AddTransactionEvent
import io.openfuture.api.component.web3.event.ProcessorEventDecoder
import io.openfuture.api.domain.transaction.EthereumTransactionDto
import io.openfuture.api.entity.scaffold.EthereumTransaction
import io.openfuture.api.repository.EthereumScaffoldRepository
import io.openfuture.api.service.EthereumTransactionService
import io.openfuture.api.util.EthereumUtils.toChecksumAddress
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.client.RestTemplate
import org.web3j.protocol.core.methods.response.Log

@Component
class TransactionHandler(
        private val service: EthereumTransactionService,
        private val repositoryEthereum: EthereumScaffoldRepository,
        private val eventDecoder: ProcessorEventDecoder,
        private val publisher: ApplicationEventPublisher
) {

    companion object {
        private val log = LoggerFactory.getLogger(TransactionHandler::class.java)
    }


    @Transactional
    fun handle(transactionLog: Log) {
        val contract = repositoryEthereum.findByAddressIgnoreCase(toChecksumAddress(transactionLog.address)) ?: return
        if (null != service.find(transactionLog.transactionHash, transactionLog.logIndexRaw)) return
        val transaction = service.save(EthereumTransaction.of(contract, transactionLog))

        try {
            val event = eventDecoder.getEvent(contract.address, transaction.data)
            val transactionDto = EthereumTransactionDto(transaction, event)
            contract.webHook?.let { RestTemplate().postForLocation(it, transactionDto) }
            publisher.publishEvent(AddTransactionEvent(this, transactionDto))
        } catch (e: Exception) {
            log.warn(e.message)
        }
    }

}