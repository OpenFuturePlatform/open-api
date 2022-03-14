package io.openfuture.api.component.state

import io.openfuture.api.domain.key.KeyWalletDto
import io.openfuture.api.domain.state.*
import io.openfuture.api.domain.widget.PaymentWidgetResponse
import io.openfuture.api.entity.state.Blockchain
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate

@Component
class DefaultStateApi(private val stateRestTemplate: RestTemplate) : StateApi {

    override fun createWallet(address: String, webHook: String, blockchain: Blockchain): StateWalletDto {
        val request = CreateStateWalletRequest(address, webHook, blockchain)
        //val request = CreateStateWalletRequestMetadata(webHook, listOf(KeyWalletDto(address,blockchain.getValue())), WalletMetaData())
        val response = stateRestTemplate.postForEntity("/wallets", request, StateWalletDto::class.java)
        return response.body!!
    }

    override fun createWalletWithMetadata(request: CreateStateWalletRequestMetadata) {
        stateRestTemplate.postForEntity("/wallets", request, Void::class.java)
    }

    override fun deleteWallet(address: String, blockchain: Blockchain) {
        val url = "/wallets/blockchain/${blockchain.getValue()}/address/${address}"
        stateRestTemplate.delete(url)
    }

    override fun getAddressTransactionsByAddress(address: String): StateWalletTransactionDetail {
        val url = "/wallets/transactions/address/${address}"
        return stateRestTemplate.getForEntity(url, StateWalletTransactionDetail::class.java).body!!
    }

    override fun getAddressTransactionsByOrder(orderKey: String): StateWalletTransaction {
        val url = "/wallets/transactions/order/${orderKey}"
        return stateRestTemplate.getForEntity(url, StateWalletTransaction::class.java).body!!
    }

    override fun getPaymentDetailByOrder(orderKey: String): PaymentWidgetResponse {
        val url = "/orders/${orderKey}"
        return stateRestTemplate.getForEntity(url, PaymentWidgetResponse::class.java).body!!
    }

}
