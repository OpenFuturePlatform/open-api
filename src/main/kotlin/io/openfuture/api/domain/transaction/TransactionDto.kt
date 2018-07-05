package io.openfuture.api.domain.transaction

import io.openfuture.api.domain.event.Event
import io.openfuture.api.domain.scaffold.ScaffoldDto
import io.openfuture.api.entity.scaffold.Transaction

data class TransactionDto(
        val scaffold: ScaffoldDto,
        val event: Event
) {

    constructor(transaction: Transaction, event: Event): this(ScaffoldDto(transaction.scaffold), event)

}