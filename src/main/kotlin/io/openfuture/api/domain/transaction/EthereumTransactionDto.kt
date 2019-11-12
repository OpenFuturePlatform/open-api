package io.openfuture.api.domain.transaction

import io.openfuture.api.domain.event.Event
import io.openfuture.api.domain.scaffold.EthereumScaffoldDto
import io.openfuture.api.entity.scaffold.EthereumTransaction
import java.util.*

data class EthereumTransactionDto(
        val scaffold: EthereumScaffoldDto,
        val event: Event,
        val date: Date
) {

    constructor(transaction: EthereumTransaction, event: Event): this(EthereumScaffoldDto(transaction.ethereumScaffold), event, transaction.date)

}