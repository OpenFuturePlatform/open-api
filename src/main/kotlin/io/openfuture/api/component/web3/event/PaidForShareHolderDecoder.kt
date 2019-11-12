package io.openfuture.api.component.web3.event

import io.openfuture.api.domain.event.PaidForShareHolderEvent
import io.openfuture.api.util.EthereumUtils.toChecksumAddress
import org.web3j.abi.TypeReference
import org.web3j.abi.datatypes.Address
import org.web3j.abi.datatypes.generated.Uint256
import org.web3j.utils.Convert.Unit.ETHER
import org.web3j.utils.Convert.fromWei
import java.math.BigInteger

class PaidForShareHolderDecoder : Decoder<PaidForShareHolderEvent> {

    override fun decode(addressScaffold: String, rawData: String): PaidForShareHolderEvent {
        val response = getResponse(rawData, listOf(object : TypeReference<Uint256>() {}, object : TypeReference<Address>() {},
                object : TypeReference<Uint256>() {}))

        val userAddress = toChecksumAddress(response[1].value as String)
        val amount = fromWei((response[2].value as BigInteger).toBigDecimal(), ETHER)

        return PaidForShareHolderEvent(userAddress, amount)
    }

}