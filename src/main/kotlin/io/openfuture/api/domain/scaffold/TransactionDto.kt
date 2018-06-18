package io.openfuture.api.domain.scaffold

import io.openfuture.api.entity.scaffold.Transaction

data class TransactionDto(
        val scaffold: ScaffoldDto,
        val data: String,
        val type: String
) {

    constructor(transaction: Transaction): this(ScaffoldDto(transaction.scaffold), transaction.data, transaction.type)

}