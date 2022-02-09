package io.openfuture.api.component.key

import io.openfuture.api.domain.key.CreateKeyRequest
import io.openfuture.api.domain.key.KeyWalletDto
import io.openfuture.api.domain.key.WalletAddressResponse
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate


@Component
class DefaultKeyApi(private val keyRestTemplate: RestTemplate): KeyApi {
    override fun generateKey(createKeyRequest: CreateKeyRequest): KeyWalletDto {
        val response = keyRestTemplate.postForEntity("/key", createKeyRequest, KeyWalletDto::class.java)
        return response.body!!
    }

    override fun getAllKeysByApplication(applicationId: String): Array<KeyWalletDto> {
        val response = keyRestTemplate.getForEntity("/key?applicationId={applicationId}", Array<KeyWalletDto>::class.java, applicationId)
        return response.body!!
    }

    override fun getApplicationByAddress(address: String): WalletAddressResponse {
        val response = keyRestTemplate.getForEntity("/key/address/{address}", WalletAddressResponse::class.java, address)
        return response.body!!
    }

    override fun deleteAllKeysByApplicationAddress(applicationId: String, address: String) {
        val url = "/key?applicationId=${applicationId}&address=${address}"
        keyRestTemplate.delete(url)
    }
}