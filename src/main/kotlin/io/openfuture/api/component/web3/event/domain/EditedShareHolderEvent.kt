package io.openfuture.api.component.web3.event.domain

import io.openfuture.api.component.web3.event.EventType
import java.math.BigInteger

data class EditedShareHolderEvent(
        val userAddress: String,
        val partnerShare: BigInteger
) : Event {

    override fun getType(): EventType = EventType.EDITED_SHARE_HOLDER

}