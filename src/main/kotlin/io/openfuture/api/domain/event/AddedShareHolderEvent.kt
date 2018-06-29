package io.openfuture.api.domain.event

import io.openfuture.api.component.web3.event.EventType
import io.openfuture.api.component.web3.event.EventType.ADDED_SHARE_HOLDER
import java.math.BigInteger

data class AddedShareHolderEvent(
        val userAddress: String,
        val partnerShare: BigInteger
) : Event {

    override fun getType(): EventType = ADDED_SHARE_HOLDER

}