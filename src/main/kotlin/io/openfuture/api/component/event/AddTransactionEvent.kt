package io.openfuture.api.component.event

import io.openfuture.api.domain.transaction.EthereumTransactionDto
import org.springframework.context.ApplicationEvent

class AddTransactionEvent(source: Any, val transaction: EthereumTransactionDto) : ApplicationEvent(source)