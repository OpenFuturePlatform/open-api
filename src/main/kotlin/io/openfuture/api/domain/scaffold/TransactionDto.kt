package io.openfuture.api.domain.scaffold

import io.openfuture.api.domain.event.Event
import io.openfuture.api.entity.scaffold.Transaction

data class TransactionDto(
        val scaffold: ScaffoldDto,
        val event: Event,
        val type: String
) {

    constructor(transaction: Transaction, event: Event): this(ScaffoldDto(transaction.scaffold), event, transaction.type)

}