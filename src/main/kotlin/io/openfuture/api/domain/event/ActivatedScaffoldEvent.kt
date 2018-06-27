package io.openfuture.api.domain.event

import io.openfuture.api.component.web3.event.EventType
import io.openfuture.api.component.web3.event.EventType.ACTIVATED_SCAFFOLD

data class ActivatedScaffoldEvent(
        val activated: Boolean
) : Event {

    override fun getType(): EventType = ACTIVATED_SCAFFOLD

}