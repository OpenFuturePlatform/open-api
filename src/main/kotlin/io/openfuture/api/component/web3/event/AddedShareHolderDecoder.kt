package io.openfuture.api.component.web3.event

import io.openfuture.api.domain.event.AddedShareHolderEvent
import io.openfuture.api.util.EthereumUtils.toChecksumAddress
import org.web3j.abi.TypeReference
import org.web3j.abi.datatypes.Address
import org.web3j.abi.datatypes.generated.Uint256
import java.math.BigInteger

class AddedShareHolderDecoder : Decoder<AddedShareHolderEvent> {

    override fun decode(addressScaffold: String, rawData: String): AddedShareHolderEvent {
        val response = getResponse(rawData, listOf(object : TypeReference<Uint256>() {}, object : TypeReference<Address>() {},
                object : TypeReference<Uint256>() {}))

        val userAddress = toChecksumAddress(response[1].value as String)
        val partnerShare = response[2].value as BigInteger

        return AddedShareHolderEvent(userAddress, partnerShare)
    }

}