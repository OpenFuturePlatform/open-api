package io.openfuture.api.component.web3.event.domain

import io.openfuture.api.component.web3.event.EventType
import java.math.BigInteger

data class FundsDepositedEvent(
        val amount: BigInteger,
        val toAddress: String
) : Event {

    override fun getType(): EventType = EventType.FUNDS_DEPOSITED

}