package io.openfuture.api.component.web3.event.decoder

import io.openfuture.api.component.web3.event.domain.PayedForShareHolderEvent
import org.web3j.abi.TypeReference
import org.web3j.abi.datatypes.Address
import org.web3j.abi.datatypes.generated.Uint256
import org.web3j.abi.datatypes.generated.Uint8
import java.math.BigInteger

class PayedForShareHolderDecoder : Decoder<PayedForShareHolderEvent> {

    override fun decode(addressScaffold: String, rawData: String): PayedForShareHolderEvent {
        val response = Decoder.getResponse(rawData, getSignature())

        val userAddress: String = response[1].value as String
        val amount: BigInteger = response[2].value as BigInteger

        return PayedForShareHolderEvent(userAddress, amount)
    }

    private fun getSignature(): List<TypeReference<*>> = listOf(
            object : TypeReference<Uint8>() {},
            object : TypeReference<Address>() {},
            object : TypeReference<Uint256>() {}
    )

}