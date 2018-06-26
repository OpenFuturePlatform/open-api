package io.openfuture.api.component.web3.event.decoder

import io.openfuture.api.component.web3.event.domain.FundsDepositedEvent
import org.web3j.abi.TypeReference
import org.web3j.abi.datatypes.Address
import org.web3j.abi.datatypes.generated.Uint256
import java.math.BigInteger

class FundsDepositedDecoder : Decoder<FundsDepositedEvent> {

    override fun decode(addressScaffold: String, rawData: String): FundsDepositedEvent {
        val response = getResponse(rawData, listOf(object : TypeReference<Uint256>() {}, object : TypeReference<Uint256>() {},
                object : TypeReference<Address>() {}))

        val amount = response[1].value as BigInteger
        val toAddress = response[2].value as String

        return FundsDepositedEvent(amount, toAddress)
    }

}