package io.openfuture.api.service

import io.openfuture.api.entity.scaffold.Scaffold
import io.openfuture.api.entity.scaffold.Transaction
import io.openfuture.api.repository.TransactionRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * @author Kadach Alexey
 */
@Service
class DefaultTransactionService(
        private val repository: TransactionRepository
) : TransactionService {

    @Transactional(readOnly = true)
    override fun getAll(scaffold: Scaffold, pageRequest: Pageable): Page<Transaction> =
            repository.findAllByScaffold(scaffold)

    @Transactional
    override fun save(transaction: Transaction): Transaction = repository.save(transaction)

}