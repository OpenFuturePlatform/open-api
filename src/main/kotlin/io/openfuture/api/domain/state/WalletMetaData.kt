package io.openfuture.api.domain.state

data class WalletMetaData(
    var amount: String,
    var orderKey: String,
    var productCurrency: String,
    var source: String,
    var test: Boolean
)
