package io.openfuture.api.component.web3.event.domain

import io.openfuture.api.component.web3.event.EventType

data class DeletedShareHolderEvent(
        val userAddress: String
) : Event {

    override fun getType(): EventType = EventType.DELETED_SHARE_HOLDER

}