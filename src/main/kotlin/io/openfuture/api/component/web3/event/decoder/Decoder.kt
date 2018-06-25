package io.openfuture.api.component.web3.event.decoder

import io.openfuture.api.component.web3.event.domain.Event
import org.web3j.abi.FunctionReturnDecoder
import org.web3j.abi.TypeReference
import org.web3j.abi.Utils
import org.web3j.abi.datatypes.Type

interface Decoder<T : Event> {

    fun decode(addressScaffold: String, rawData: String): T

    fun getResponse(rawData: String, signature: List<TypeReference<*>>): List<Type<Any>> =
            FunctionReturnDecoder.decode(rawData, Utils.convert(signature))

}