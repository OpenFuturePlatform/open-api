package io.openfuture.api.domain.event

import io.openfuture.api.component.web3.event.EventType
import io.openfuture.api.component.web3.event.EventType.PAYMENT_COMPLETED
import java.math.BigDecimal
import java.math.BigInteger

data class PaymentCompletedEvent(
        val customerAddress: String,
        val transactionAmount: BigDecimal,
        val scaffoldTransactionIndex: BigInteger,
        val properties: Map<String, Any>
) : Event {

    override fun getType(): EventType = PAYMENT_COMPLETED

}