package io.openfuture.api.domain.key

import com.fasterxml.jackson.annotation.JsonProperty
import io.openfuture.api.entity.application.BlockchainType

data class ImportWalletRequest(
    @JsonProperty("applicationId")
    var applicationId: String,

    @JsonProperty("webHook")
    var webHook: String,

    @JsonProperty("address")
    var address: String,

    @JsonProperty("blockchainType")
    val blockchainType: BlockchainType
)
