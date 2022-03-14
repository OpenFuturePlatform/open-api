package io.openfuture.api.domain.state

import io.openfuture.api.domain.transaction.TransactionDto

data class StateWalletTransaction(
    val address: String,
    val blockchain: String,
    val transactions: List<TransactionDto>
)
