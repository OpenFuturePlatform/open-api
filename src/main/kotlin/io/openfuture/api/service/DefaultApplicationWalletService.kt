package io.openfuture.api.service

import io.openfuture.api.component.key.KeyApi
import io.openfuture.api.component.state.StateApi
import io.openfuture.api.domain.key.CreateKeyRequest
import io.openfuture.api.domain.key.GenerateWalletRequest
import io.openfuture.api.domain.key.KeyWalletDto
import io.openfuture.api.entity.auth.User
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
@Transactional
class DefaultApplicationWalletService(
    private val keyApi: KeyApi,
    private val stateApi: StateApi
): ApplicationWalletService {

    override fun generateWallet(request: GenerateWalletRequest): KeyWalletDto {
        // Generate address
        val keyWalletDto  = keyApi.generateKey(CreateKeyRequest(request.applicationId, request.webHook, request.blockchainType))

        // Save webhook on state
        //request.webHook.let { stateApi.createWallet(keyWalletDto.address, it, Blockchain.valueOf(request.blockchainType.getValue())) }

        return keyWalletDto
    }

    override fun getAllWallets(id: Long): Array<KeyWalletDto> {
        return keyApi.getAllKeysByApplication(id.toString())
    }

    override fun deleteWallet(applicationId: String, address: String) {
        keyApi.deleteAllKeysByApplicationAddress(applicationId, address)
    }
}