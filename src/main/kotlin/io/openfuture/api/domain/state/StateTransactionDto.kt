package io.openfuture.api.domain.state

class StateTransactionDto(
        val id: Long,
        val hash: String,
        val amount: Long,
        val address: String,
        val type: String,
        val participant: String,
        val date: Long,
        val externalHash: String,
        val blockHeight: Long,
        val blockHash: String
)
