package io.openfuture.api.component.web3.event

import io.openfuture.api.domain.event.PaymentCompletedEvent
import io.openfuture.api.entity.scaffold.PropertyType.*
import io.openfuture.api.entity.scaffold.ScaffoldProperty
import io.openfuture.api.repository.ScaffoldPropertyRepository
import org.web3j.abi.TypeReference
import org.web3j.abi.datatypes.Address
import org.web3j.abi.datatypes.Bool
import org.web3j.abi.datatypes.generated.Bytes32
import org.web3j.abi.datatypes.generated.Uint256
import java.math.BigInteger

class PaymentCompletedDecoder(private val scaffoldPropertyRepository: ScaffoldPropertyRepository) : Decoder<PaymentCompletedEvent> {

    override fun decode(addressScaffold: String, rawData: String): PaymentCompletedEvent {
        val scaffoldProperties = scaffoldPropertyRepository.findAllByScaffoldAddress(addressScaffold)

        val response = getResponse(rawData, getSignature(scaffoldProperties))

        val customerAddress = response[1].value as String
        val transactionAmount = response[2].value as BigInteger
        val scaffoldTransactionIndex = response[3].value as BigInteger

        val properties = mutableMapOf<String, Any>()
        val startPropertyIndex = 4
        for ((index, property) in scaffoldProperties.withIndex()) {
            properties[property.name] = when (property.getType()) {
                STRING -> String(response[startPropertyIndex + index].value as ByteArray)
                NUMBER -> response[startPropertyIndex + index].value as BigInteger
                BOOLEAN -> response[startPropertyIndex + index].value as Boolean
            }
        }

        return PaymentCompletedEvent(customerAddress, transactionAmount, scaffoldTransactionIndex, properties)
    }

    private fun getSignature(properties: List<ScaffoldProperty>): List<TypeReference<*>> {
        val signature = mutableListOf(
                object : TypeReference<Uint256>() {},
                object : TypeReference<Address>() {},
                object : TypeReference<Uint256>() {},
                object : TypeReference<Uint256>() {}
        )

        for (property in properties) {
            when (property.getType()) {
                STRING -> signature.add(object : TypeReference<Bytes32>() {})
                NUMBER -> signature.add(object : TypeReference<Uint256>() {})
                BOOLEAN -> signature.add(object : TypeReference<Bool>() {})
            }
        }

        return signature
    }

}