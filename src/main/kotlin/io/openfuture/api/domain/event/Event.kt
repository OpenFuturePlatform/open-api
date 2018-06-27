package io.openfuture.api.domain.event

import io.openfuture.api.component.web3.event.EventType

interface Event {

    fun getType(): EventType

}