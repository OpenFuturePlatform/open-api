package io.openfuture.api.domain.transaction

import io.openfuture.api.domain.event.Event
import io.openfuture.api.domain.scaffold.ScaffoldDto
import io.openfuture.api.entity.scaffold.Transaction
import java.util.*

data class TransactionDto(
        val scaffold: ScaffoldDto,
        val event: Event,
        val date: Date
) {

    constructor(transaction: Transaction, event: Event): this(ScaffoldDto(transaction.scaffold), event, transaction.date)

}