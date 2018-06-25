package io.openfuture.api.component.web3.event

import io.openfuture.api.component.web3.event.decoder.*
import io.openfuture.api.component.web3.event.domain.Event
import io.openfuture.api.repository.ScaffoldPropertyRepository
import org.springframework.stereotype.Component
import java.math.BigInteger
import javax.annotation.PostConstruct

@Component
class ProcessorEventDecoder(private val scaffoldPropertyRepository: ScaffoldPropertyRepository) {

    private lateinit var decoders: List<Decoder<out Event>>


    @PostConstruct
    private fun initDecoders() {
        decoders = listOf(
                ActivatedScaffoldDecoder(),
                AddedShareHolderDecoder(),
                DeletedShareHolderDecoder(),
                EditedShareHolderDecoder(),
                FundsDepositedDecoder(),
                PayedForShareHolderDecoder(),
                PaymentCompletedDecoder(scaffoldPropertyRepository)
        )
    }

    fun getEvent(addressScaffold: String, rawData: String): Event {
        val response = Decoder.getResponse(rawData, Decoder.getEventTypeSignature())
        val orderEventType = response[0].value as BigInteger
        val eventType = EventType.getEventTypeByOrder(orderEventType.toInt())

        val decoder = decoders.first { eventType.decoderClass == it.javaClass }

        return decoder.decode(addressScaffold, rawData)
    }

}