package io.openfuture.api.domain.state

import com.fasterxml.jackson.annotation.JsonProperty
import io.openfuture.api.entity.application.BlockchainType

data class WalletApiStateRequest(
    @JsonProperty("address")
    var address: String,
    @JsonProperty("blockchain")
    val blockchain: BlockchainType,
    @JsonProperty("encrypted")
    var encrypted: String,
    @JsonProperty("timestamp")
    var timestamp: String
)
