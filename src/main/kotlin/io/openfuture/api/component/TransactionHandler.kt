package io.openfuture.api.component

import io.openfuture.api.entity.scaffold.Transaction
import io.openfuture.api.repository.ScaffoldRepository
import io.openfuture.api.service.TransactionService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.web3j.protocol.core.methods.response.Log

/**
 * @author Kadach Alexey
 */
@Component
class TransactionHandler(
        private val service: TransactionService,
        private val scaffoldRepository: ScaffoldRepository
) {

    companion object {
        private val log = LoggerFactory.getLogger(TransactionHandler::class.java)
    }

    fun handle(transactionLog: Log) {
        val scaffold = scaffoldRepository.findByAddress(transactionLog.address)

        if (null == scaffold) {
            log.warn("Scaffold with address ${transactionLog.address} not found")
            return
        }

        service.save(Transaction.of(scaffold, transactionLog))
    }

}