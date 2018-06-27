package io.openfuture.api.domain.event

import io.openfuture.api.component.web3.event.EventType
import io.openfuture.api.component.web3.event.EventType.DELETED_SHARE_HOLDER

data class DeletedShareHolderEvent(
        val userAddress: String
) : Event {

    override fun getType(): EventType = DELETED_SHARE_HOLDER

}