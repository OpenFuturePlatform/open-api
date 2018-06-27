package io.openfuture.api.domain.event

import io.openfuture.api.component.web3.event.EventType
import io.openfuture.api.component.web3.event.EventType.EDITED_SHARE_HOLDER
import java.math.BigInteger

data class EditedShareHolderEvent(
        val userAddress: String,
        val partnerShare: BigInteger
) : Event {

    override fun getType(): EventType = EDITED_SHARE_HOLDER

}