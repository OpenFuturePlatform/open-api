package io.openfuture.api.component.web3.event.domain

import io.openfuture.api.component.web3.event.EventType

interface Event {

    fun getType(): EventType

}