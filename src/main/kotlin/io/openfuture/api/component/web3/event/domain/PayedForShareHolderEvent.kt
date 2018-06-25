package io.openfuture.api.component.web3.event.domain

import io.openfuture.api.component.web3.event.EventType
import java.math.BigInteger

data class PayedForShareHolderEvent(
        val userAddress: String,
        val amount: BigInteger
) : Event {

    override fun getType(): EventType = EventType.PAYED_FOR_SHARE_HOLDER

}