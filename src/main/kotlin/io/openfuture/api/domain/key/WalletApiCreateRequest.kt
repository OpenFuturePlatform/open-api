package io.openfuture.api.domain.key

import com.fasterxml.jackson.annotation.JsonProperty
import io.openfuture.api.entity.application.BlockchainType

data class WalletApiCreateRequest(
    @JsonProperty("metadata")
    var metadata: WalletMetaDto
)