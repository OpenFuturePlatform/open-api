package io.openfuture.api.component.listener.event

import io.openfuture.api.domain.transaction.TransactionDto
import org.springframework.context.ApplicationEvent

class AddTransactionEvent(source: Any, val transaction: TransactionDto) : ApplicationEvent(source)