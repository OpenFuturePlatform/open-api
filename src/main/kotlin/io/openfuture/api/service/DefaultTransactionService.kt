package io.openfuture.api.service

import io.openfuture.api.entity.scaffold.Transaction
import io.openfuture.api.repository.TransactionRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * @author Kadach Alexey
 */
@Service
class DefaultTransactionService(
        private val repository: TransactionRepository
) : TransactionService {

    @Transactional
    override fun save(transaction: Transaction): Transaction = repository.save(transaction)

}