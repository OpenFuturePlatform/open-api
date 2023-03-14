package io.openfuture.api.component.state

import io.openfuture.api.domain.state.WalletApiStateResponse
import io.openfuture.api.domain.state.*
import io.openfuture.api.domain.transaction.TransactionDto
import io.openfuture.api.domain.widget.PaymentWidgetResponse
import io.openfuture.api.entity.state.Blockchain
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate

@Component
class DefaultStateApi(private val stateRestTemplate: RestTemplate) : StateApi {

    override fun createWallet(
        address: String,
        webHook: String,
        blockchain: Blockchain,
        applicationId: String
    ): StateWalletDto {
        val request = CreateStateWalletRequest(address, webHook, blockchain.getValue(), applicationId)
        val response = stateRestTemplate.postForEntity("/wallets/single", request, StateWalletDto::class.java)
        return response.body!!
    }

    override fun createWalletWithMetadata(request: CreateStateWalletRequestMetadata): CreateStateWalletResponse {
        return stateRestTemplate.postForEntity("/api/wallets", request, CreateStateWalletResponse::class.java).body!!
    }

    override fun createWallet(request: CreateStateWithUserRequest): AddWatchResponse {
        return stateRestTemplate.postForEntity("/api/wallets/v2/add", request, AddWatchResponse::class.java).body!!
    }

    override fun updateWalletWithMetadata(request: UpdateStateWalletMetadata) {
        stateRestTemplate.postForEntity("/wallets/update", request, Void::class.java)
    }

    override fun deleteWallet(address: String, blockchain: Blockchain) {
        val url = "/wallets/blockchain/${blockchain.getValue()}/address/${address}"
        stateRestTemplate.delete(url)
    }

    override fun getWallet(address: String, blockchain: Blockchain): WalletApiStateResponse {
        val url = "/wallets/blockchain/${blockchain.getValue()}/address/${address}"
        return stateRestTemplate.getForEntity(url, WalletApiStateResponse::class.java).body!!
    }

    override fun getAddressTransactionsByAddress(address: String): StateWalletTransactionDetail {
        val url = "/wallets/transactions/address/${address}"
        return stateRestTemplate.getForEntity(url, StateWalletTransactionDetail::class.java).body!!
    }

    override fun getTransactionsByAddress(address: String): Array<TransactionDto> {
        val url = "/wallets/transactions/${address}"
        return stateRestTemplate.getForEntity(url, Array<TransactionDto>::class.java).body!!
    }

    override fun getPaymentDetailByOrder(orderKey: String): PaymentWidgetResponse {
        val url = "/orders/${orderKey}"
        return stateRestTemplate.getForEntity(url, PaymentWidgetResponse::class.java).body!!
    }

    override fun getOrderDetailsByApplication(applicationId: String): Array<StateOrderDetail> {
        val url = "/wallets/application/${applicationId}"
        return stateRestTemplate.getForEntity(url, Array<StateOrderDetail>::class.java).body!!
    }

}
