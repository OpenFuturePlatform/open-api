package io.openfuture.api.component.web3.event.domain

import io.openfuture.api.component.web3.event.EventType
import io.openfuture.api.component.web3.event.EventType.PAYED_FOR_SHARE_HOLDER
import java.math.BigInteger

data class PayedForShareHolderEvent(
        val userAddress: String,
        val amount: BigInteger
) : Event {

    override fun getType(): EventType = PAYED_FOR_SHARE_HOLDER

}