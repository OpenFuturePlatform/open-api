package io.openfuture.api.domain.state

import io.openfuture.api.domain.transaction.TransactionDto
import java.math.BigDecimal

data class StateWalletTransactionDetail(
    var orderKey: String,
    var amount: BigDecimal,
    var totalPaid: BigDecimal = BigDecimal.ZERO,
    var rate: BigDecimal,
    val transactions: List<TransactionDto>
)
