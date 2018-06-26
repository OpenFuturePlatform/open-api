package io.openfuture.api.component.web3.event

import io.openfuture.api.component.web3.event.decoder.*
import io.openfuture.api.component.web3.event.domain.Event
import io.openfuture.api.repository.ScaffoldPropertyRepository
import org.springframework.stereotype.Component
import org.web3j.abi.FunctionReturnDecoder
import org.web3j.abi.TypeReference
import org.web3j.abi.Utils
import org.web3j.abi.datatypes.generated.Uint256
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
        val response = FunctionReturnDecoder.decode(rawData, Utils.convert(listOf(object : TypeReference<Uint256>() {})))
        val eventTypeId = response[0].value as BigInteger
        val eventType = EventType.getById(eventTypeId.toInt())

        val decoder = decoders.first { eventType.decoderClass == it.javaClass }

        return decoder.decode(addressScaffold, rawData)
    }

}