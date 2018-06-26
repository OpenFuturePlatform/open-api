package io.openfuture.api.component.web3.event

import io.openfuture.api.component.web3.event.decoder.*
import io.openfuture.api.component.web3.event.domain.Event
import io.openfuture.api.entity.base.Dictionary
import io.openfuture.api.exception.EventTypeException

enum class EventType(private val id: Int, val decoderClass: Class<out Decoder<out Event>>) : Dictionary {

    PAYMENT_COMPLETED(1, PaymentCompletedDecoder::class.java),
    FUNDS_DEPOSITED(2, FundsDepositedDecoder::class.java),
    ACTIVATED_SCAFFOLD(3, ActivatedScaffoldDecoder::class.java),
    ADDED_SHARE_HOLDER(4, AddedShareHolderDecoder::class.java),
    EDITED_SHARE_HOLDER(5, EditedShareHolderDecoder::class.java),
    DELETED_SHARE_HOLDER(6, DeletedShareHolderDecoder::class.java),
    PAYED_FOR_SHARE_HOLDER(7, PayedForShareHolderDecoder::class.java)
    ;

    companion object {
        fun getById(id: Int): EventType {
            for (type in values()) {
                if (type.id == id) {
                    return type
                }
            }
            throw EventTypeException("There is no decoder with such $id")
        }
    }

    override fun getId(): Int = id

}