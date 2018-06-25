package io.openfuture.api.component.web3.event

import io.openfuture.api.component.web3.event.decoder.*
import io.openfuture.api.component.web3.event.domain.Event
import io.openfuture.api.exception.EventTypeException

enum class EventType(val decoderClass: Class<out Decoder<out Event>>) {

    PAYMENT_COMPLETED(PaymentCompletedDecoder::class.java),
    FUNDS_DEPOSITED(FundsDepositedDecoder::class.java),
    ACTIVATED_SCAFFOLD(ActivatedScaffoldDecoder::class.java),
    ADDED_SHARE_HOLDER(AddedShareHolderDecoder::class.java),
    EDITED_SHARE_HOLDER(EditedShareHolderDecoder::class.java),
    DELETED_SHARE_HOLDER(DeletedShareHolderDecoder::class.java),
    PAYED_FOR_SHARE_HOLDER(PayedForShareHolderDecoder::class.java)
    ;

    companion object {
        fun getEventTypeByOrder(order: Int): EventType {
            for (type in values()) {
                if (type.ordinal == order) {
                    return type
                }
            }
            throw EventTypeException("There is no decoder under $order a number")
        }
    }

}