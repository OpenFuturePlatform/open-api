package io.openfuture.api.component.web3.event.decoder

import io.openfuture.api.component.web3.event.domain.FundsDepositedEvent
import org.web3j.abi.TypeReference
import org.web3j.abi.datatypes.Address
import org.web3j.abi.datatypes.generated.Uint256
import org.web3j.abi.datatypes.generated.Uint8
import java.math.BigInteger

class FundsDepositedDecoder : Decoder<FundsDepositedEvent> {

    override fun decode(addressScaffold: String, rawData: String): FundsDepositedEvent {
        val response = Decoder.getResponse(rawData, getSignature())

        val amount: BigInteger = response[1].value as BigInteger
        val toAddress: String = response[2].value as String

        return FundsDepositedEvent(amount, toAddress)
    }

    private fun getSignature(): List<TypeReference<*>> = listOf(
            object : TypeReference<Uint8>() {},
            object : TypeReference<Uint256>() {},
            object : TypeReference<Address>() {}
    )

}