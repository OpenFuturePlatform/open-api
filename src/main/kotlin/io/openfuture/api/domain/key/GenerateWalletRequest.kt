package io.openfuture.api.domain.key

import com.fasterxml.jackson.annotation.JsonProperty
import io.openfuture.api.entity.application.BlockchainType
import io.openfuture.api.entity.scaffold.Currency
import javax.validation.constraints.NotNull

data class GenerateWalletRequest(
    @JsonProperty("applicationId")
    var applicationId: String,

    @JsonProperty("webHook")
    var webHook: String,

    @JsonProperty("blockchainType")
    val blockchainType: BlockchainType
)
