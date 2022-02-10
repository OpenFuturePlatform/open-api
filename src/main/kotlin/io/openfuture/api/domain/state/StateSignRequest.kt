package io.openfuture.api.domain.state

data class StateSignRequest(
    val address: String,
    val order_id: Int,
    val status: String

    ){
}
