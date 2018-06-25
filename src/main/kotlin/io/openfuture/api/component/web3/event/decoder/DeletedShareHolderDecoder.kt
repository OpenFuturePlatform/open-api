package io.openfuture.api.component.web3.event.decoder

import io.openfuture.api.component.web3.event.domain.DeletedShareHolderEvent
import org.web3j.abi.TypeReference
import org.web3j.abi.datatypes.Address
import org.web3j.abi.datatypes.generated.Uint8

class DeletedShareHolderDecoder : Decoder<DeletedShareHolderEvent> {

    override fun decode(addressScaffold: String, rawData: String): DeletedShareHolderEvent {
        val response = Decoder.getResponse(rawData, getSignature())

        val userAddress: String = response[1].value as String

        return DeletedShareHolderEvent(userAddress)
    }

    private fun getSignature(): List<TypeReference<*>> = listOf(
            object : TypeReference<Uint8>() {},
            object : TypeReference<Address>() {}
    )

}