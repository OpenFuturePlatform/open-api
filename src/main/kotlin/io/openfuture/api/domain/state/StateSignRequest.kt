package io.openfuture.api.domain.state

data class StateSignRequest(
    val status: String,
    val order_id: Int,
    val address: String
){
    override fun toString(): String {
        return "{status='$status',order_id=$order_id,address='$address'}"
    }
}
