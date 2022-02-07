package io.openfuture.api.component.state

import io.openfuture.api.domain.state.CreateStateWalletRequest
import io.openfuture.api.domain.state.StateWalletDto
import io.openfuture.api.domain.transaction.TransactionDto
import io.openfuture.api.entity.state.Blockchain
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate

@Component
class DefaultStateApi(private val stateRestTemplate: RestTemplate) : StateApi {

    override fun createWallet(address: String, webHook: String, blockchain: Blockchain): StateWalletDto {
        val request = CreateStateWalletRequest(address, webHook, blockchain)
        val response = stateRestTemplate.postForEntity("/wallets", request, StateWalletDto::class.java)
        return response.body!!
    }

    override fun deleteWallet(address: String, blockchain: Blockchain) {
        val url = "/wallets/blockchain/${blockchain.getValue()}/address/${address}"
        stateRestTemplate.delete(url)
    }

    override fun getAddressTransactions(address: String): Array<TransactionDto> {
        val url = "/wallets/transactions/address/${address}"
        return stateRestTemplate.getForEntity(url, Array<TransactionDto>::class.java ).body!!
    }

}
