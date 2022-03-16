package io.openfuture.api.domain.transaction

import io.openfuture.api.domain.state.StateWalletIdentity
import java.math.BigDecimal
import java.time.LocalDateTime

data class TransactionDto(
    val hash: String,
    val from: String,
    val to: String,
    val amount: BigDecimal,
    val date: LocalDateTime,
    val blockHeight: Long,
    val blockHash: String,
    val rate: BigDecimal
)
