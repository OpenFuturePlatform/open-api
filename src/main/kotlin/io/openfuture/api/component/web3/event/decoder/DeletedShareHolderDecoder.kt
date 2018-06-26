package io.openfuture.api.component.web3.event.decoder

import io.openfuture.api.component.web3.event.domain.DeletedShareHolderEvent
import org.web3j.abi.TypeReference
import org.web3j.abi.datatypes.Address
import org.web3j.abi.datatypes.generated.Uint256

class DeletedShareHolderDecoder : Decoder<DeletedShareHolderEvent> {

    override fun decode(addressScaffold: String, rawData: String): DeletedShareHolderEvent {
        val response = getResponse(rawData, listOf(object : TypeReference<Uint256>() {}, object : TypeReference<Address>() {}))

        val userAddress = response[1].value as String

        return DeletedShareHolderEvent(userAddress)
    }

}