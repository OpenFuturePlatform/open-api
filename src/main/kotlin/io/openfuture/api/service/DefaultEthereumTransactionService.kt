package io.openfuture.api.service

import io.openfuture.api.entity.scaffold.EthereumScaffold
import io.openfuture.api.entity.scaffold.EthereumTransaction
import io.openfuture.api.repository.EthereumTransactionRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class DefaultEthereumTransactionService(
        private val repository: EthereumTransactionRepository
) : EthereumTransactionService {

    @Transactional(readOnly = true)
    override fun getAll(ethereumScaffold: EthereumScaffold, pageRequest: Pageable): Page<EthereumTransaction> =
            repository.findAllByEthereumScaffoldOrderByDateDesc(ethereumScaffold, pageRequest)

    @Transactional(readOnly = true)
    override fun find(hash: String, index: String): EthereumTransaction? = repository.findByHashAndIndex(hash, index)

    @Transactional
    override fun save(transaction: EthereumTransaction): EthereumTransaction = repository.save(transaction)

}