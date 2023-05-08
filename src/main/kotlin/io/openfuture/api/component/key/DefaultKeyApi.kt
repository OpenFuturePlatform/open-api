package io.openfuture.api.component.key

import io.openfuture.api.domain.key.*
import io.openfuture.keymanagementservice.dto.GenerateMultipleWalletForOrderRequest
import io.openfuture.keymanagementservice.dto.GenerateMultipleWalletForUserRequest
import io.openfuture.api.domain.key.creation.GenerateMultipleWalletRequest
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import java.util.*


@Component
class DefaultKeyApi(
    private val keyRestTemplate: RestTemplate
    ): KeyApi {

    override fun generateWallet(createKeyRequest: CreateKeyRequest): KeyWalletDto {
        val response = keyRestTemplate.postForEntity("/key", createKeyRequest, KeyWalletDto::class.java)
        return response.body!!
    }

    override fun importWallet(request: ImportKeyRequest): KeyWalletDto {
        val response = keyRestTemplate.postForEntity("/key/import", request, KeyWalletDto::class.java)
        return response.body!!
    }

    override fun importWalletV2(request: ImportWalletOpenKeyRequest): KeyWalletDto {
        val response = keyRestTemplate.postForEntity("/key/wallet", request, KeyWalletDto::class.java)
        return response.body!!
    }

    override fun generateMultipleWallets(request: GenerateMultipleWalletRequest): Array<KeyWalletDto> {
        val response = keyRestTemplate.postForEntity("/key/multiple", request, Array<KeyWalletDto>::class.java)
        return response.body!!
    }

    override fun generateMultipleWalletsWithOrder(request: GenerateMultipleWalletForOrderRequest): Array<KeyWalletDto> {
        val response = keyRestTemplate.postForEntity("/key/order", request, Array<KeyWalletDto>::class.java)
        return response.body!!
    }

    override fun generateMultipleWalletsWithUser(request: GenerateMultipleWalletForUserRequest): Array<KeyWalletDto> {
        val response = keyRestTemplate.postForEntity("/key/user", request, Array<KeyWalletDto>::class.java)
        return response.body!!
    }

    override fun updateWallets(createMultipleKeyRequest: CreateMultipleKeyRequest): Array<KeyWalletDto> {
        val response = keyRestTemplate.postForEntity("/key/update", createMultipleKeyRequest, Array<KeyWalletDto>::class.java)
        return response.body!!
    }

    override fun getAllWalletsByApplication(applicationId: String, userId: Optional<String>, orderKey: Optional<String>): Array<KeyWalletDto> {
        if (userId.isPresent){
            val response = keyRestTemplate.getForEntity("/key?applicationId={applicationId}&userId={userId}", Array<KeyWalletDto>::class.java, applicationId, userId.get())
            return response.body!!
        } else if (orderKey.isPresent){
            val url = "/key/order/${orderKey.get()}"
            val response = keyRestTemplate.getForEntity(url, Array<KeyWalletDto>::class.java)
            return response.body!!

        } else {
            val response = keyRestTemplate.getForEntity(
                "/key?applicationId={applicationId}",
                Array<KeyWalletDto>::class.java,
                applicationId
            )
            return response.body!!
        }
    }

    override fun getAllWalletsByOrderKey(orderKey: String): Array<KeyWalletDto> {
        val url = "/key/order/${orderKey}"
        val response = keyRestTemplate.getForEntity(url, Array<KeyWalletDto>::class.java)
        return response.body!!
    }

    override fun getApplicationByAddress(address: String): WalletAddressResponse {
        val response = keyRestTemplate.getForEntity("/key/address/{address}", WalletAddressResponse::class.java, address)
        return response.body!!
    }

    override fun deleteAllWalletsByApplicationAddress(applicationId: String, address: String) {
        val url = "/key?applicationId=${applicationId}&address=${address}"
        keyRestTemplate.delete(url)
    }

    override fun exportPrivateKey(keyWalletDto: KeyWalletDto): String {
        val response = keyRestTemplate.postForEntity("/key/export/private", keyWalletDto, String::class.java)
        return response.body!!
    }
}