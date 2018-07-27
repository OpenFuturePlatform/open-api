package io.openfuture.api.domain.event

import io.openfuture.api.component.web3.event.EventType
import io.openfuture.api.component.web3.event.EventType.FUNDS_DEPOSITED
import java.math.BigDecimal

data class FundsDepositedEvent(
        val amount: BigDecimal,
        val toAddress: String
) : Event {

    override fun getType(): EventType = FUNDS_DEPOSITED

}