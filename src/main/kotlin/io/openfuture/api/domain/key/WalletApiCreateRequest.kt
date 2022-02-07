package io.openfuture.api.domain.key

import com.fasterxml.jackson.annotation.JsonProperty
import io.openfuture.api.entity.application.BlockchainType

data class WalletApiCreateRequest(
    @JsonProperty("blockchain")
    val blockchain: BlockchainType,
    @JsonProperty("timestamp")
    var timestamp: String,
    @JsonProperty("metadata")
    var metadata: WalletMetaDto?
){
    override fun toString(): String {
        return "blockchain='$blockchain'&timestamp='$timestamp'"
    }
}