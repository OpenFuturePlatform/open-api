package io.openfuture.api.service

import io.openfuture.api.component.key.KeyApi
import io.openfuture.api.component.state.StateApi
import io.openfuture.api.domain.key.CreateKeyRequest
import io.openfuture.api.domain.key.GenerateWalletRequest
import io.openfuture.api.domain.key.KeyWalletDto
import io.openfuture.api.entity.auth.User
import io.openfuture.api.entity.state.Blockchain
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
@Transactional
class DefaultApplicationWalletService(
    private val keyApi: KeyApi,
    private val stateApi: StateApi
): ApplicationWalletService {

    override fun generateWallet(request: GenerateWalletRequest, user: User): KeyWalletDto {
        // Generate address
        val keyWalletDto  = keyApi.generateKey(CreateKeyRequest(request.applicationId, user.id.toString(), request.blockchainType))

        // Save webhook on state
        //request.webHook.let { stateApi.createWallet(keyWalletDto.address, it, Blockchain.Ethereum) }

        return keyWalletDto
    }

    override fun getAllWallets(id: Long): Array<KeyWalletDto> {
        return keyApi.getAllKeysByApplication(id.toString())
    }

    override fun deleteWallet(applicationId: String, address: String) {
        // Delete from Open Key
        keyApi.deleteAllKeysByApplicationAddress(applicationId, address)
        // Delete from Open State
        //stateApi.deleteWallet(address, Blockchain.Ethereum)
    }
}