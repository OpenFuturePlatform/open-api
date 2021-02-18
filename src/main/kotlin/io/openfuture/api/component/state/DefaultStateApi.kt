package io.openfuture.api.component.state

import io.openfuture.api.domain.state.CreateStateWalletRequest
import io.openfuture.api.domain.state.StateWalletDto
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

}
