package io.openfuture.api.domain.key

import com.fasterxml.jackson.annotation.JsonProperty
import io.openfuture.api.entity.application.BlockchainType

data class WalletMetaDto(
    @JsonProperty("amount")
    var amount: String,
    @JsonProperty("orderId")
    var orderId: String,
    @JsonProperty("orderKey")
    var orderKey: String,
    @JsonProperty("paymentCurrency")
    val paymentCurrency: List<BlockchainType>,
    @JsonProperty("productCurrency")
    var productCurrency: String,
    @JsonProperty("source")
    var source: String,
    @JsonProperty("test")
    var test: Boolean
)
