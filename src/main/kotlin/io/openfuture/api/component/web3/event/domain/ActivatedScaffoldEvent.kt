package io.openfuture.api.component.web3.event.domain

import io.openfuture.api.component.web3.event.EventType

data class ActivatedScaffoldEvent(
        val activated: Boolean
) : Event {

    override fun getType(): EventType = EventType.ACTIVATED_SCAFFOLD

}