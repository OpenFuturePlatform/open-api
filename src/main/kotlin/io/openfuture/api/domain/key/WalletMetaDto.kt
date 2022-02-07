package io.openfuture.api.domain.key

import com.fasterxml.jackson.annotation.JsonProperty
import io.openfuture.api.entity.application.BlockchainType

data class WalletMetaDto(
    @JsonProperty("order_id")
    var orderId: String,
    @JsonProperty("order_key")
    var orderKey: String,
    @JsonProperty("amount")
    var amount: String,
    @JsonProperty("product_currency")
    var productCurrency: String,
    @JsonProperty("source")
    var source: String,
    @JsonProperty("payment_currency")
    val paymentCurrency: BlockchainType
)
