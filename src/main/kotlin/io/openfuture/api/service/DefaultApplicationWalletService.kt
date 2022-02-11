package io.openfuture.api.service

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.openfuture.api.component.key.KeyApi
import io.openfuture.api.component.state.StateApi
import io.openfuture.api.domain.key.CreateKeyRequest
import io.openfuture.api.domain.key.GenerateWalletRequest
import io.openfuture.api.domain.key.KeyWalletDto
import io.openfuture.api.domain.state.StateSignRequest
import io.openfuture.api.domain.transaction.TransactionDto
import io.openfuture.api.entity.auth.User
import io.openfuture.api.entity.state.Blockchain
import io.openfuture.api.util.KeyGeneratorUtils
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
@Transactional
class DefaultApplicationWalletService(
    private val keyApi: KeyApi,
    private val stateApi: StateApi,
    private val applicationService: ApplicationService
): ApplicationWalletService {

    override fun generateWallet(request: GenerateWalletRequest, user: User): KeyWalletDto {
        // Generate address on open key
        val keyWalletDto  = keyApi.generateKey(CreateKeyRequest(request.applicationId, user.id.toString(), request.blockchainType))

        // Save webhook on open state
        request.webHook.let { stateApi.createWallet(keyWalletDto.address, it, Blockchain.Ethereum) }

        return keyWalletDto
    }

    override fun getAllWallets(id: Long): Array<KeyWalletDto> {
        return keyApi.getAllKeysByApplication(id.toString())
    }

    override fun deleteWallet(applicationId: String, address: String) {
        // Delete from Open Key
        keyApi.deleteAllKeysByApplicationAddress(applicationId, address)
        // Delete from Open State
        stateApi.deleteWallet(address, Blockchain.Ethereum)
    }

    override fun getAddressTransactions(address: String): Array<TransactionDto> {
        return stateApi.getAddressTransactions(address)
    }

    override fun generateSignature(address: String, request: StateSignRequest): String {
        val walletAddressResponse = keyApi.getApplicationByAddress(address)
        val application = applicationService.getById(walletAddressResponse.applicationId.toLong())
        val mapper = jacksonObjectMapper()
        val str = mapper.writeValueAsString(request)
        val hmacSha256 = application.let {
            KeyGeneratorUtils.calcHmacSha256(it.apiSecretKey, str)
        }
        return hmacSha256
    }
}