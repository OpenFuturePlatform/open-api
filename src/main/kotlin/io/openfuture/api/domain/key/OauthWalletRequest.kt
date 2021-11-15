package io.openfuture.api.domain.key

import com.fasterxml.jackson.annotation.JsonProperty
import io.openfuture.api.entity.application.BlockchainType

data class OauthWalletRequest(
    @JsonProperty("timestamp")
    var timestamp: String,
    @JsonProperty("blockchain")
    val blockchain: BlockchainType
){
    override fun toString(): String {
        return "{timestamp='$timestamp', blockchain='$blockchain'}"
    }
}