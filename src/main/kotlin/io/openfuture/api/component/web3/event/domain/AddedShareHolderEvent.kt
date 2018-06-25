package io.openfuture.api.component.web3.event.domain

import io.openfuture.api.component.web3.event.EventType
import java.math.BigInteger

data class AddedShareHolderEvent(
        val userAddress: String,
        val partnerShare: BigInteger
) : Event {

    override fun getType(): EventType = EventType.ADDED_SHARE_HOLDER

}