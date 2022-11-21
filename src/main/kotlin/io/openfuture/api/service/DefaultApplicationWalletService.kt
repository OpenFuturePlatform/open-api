package io.openfuture.api.service

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.openfuture.api.component.key.KeyApi
import io.openfuture.api.component.state.StateApi
import io.openfuture.api.domain.key.*
import io.openfuture.api.domain.state.StateSignRequest
import io.openfuture.api.domain.state.StateWalletTransactionDetail
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
) : ApplicationWalletService {

    override fun generateWallet(request: GenerateWalletRequest, user: User): KeyWalletDto {
        // Generate address on open key
        val keyWalletDto = keyApi.generateWallet(CreateKeyRequest(request.applicationId, user.id.toString(), request.blockchainType))

        // Save webhook on open state
        stateApi.createWallet(keyWalletDto.address, request.webHook, Blockchain.getBlockchainBySymbol(request.blockchainType.getValue()), request.applicationId)

        return keyWalletDto
    }

    override fun importWallet(request: ImportWalletRequest, user: User) {
        keyApi.importWallet(ImportKeyRequest(request.applicationId, user.id.toString(), request.blockchainType, request.address, ""))
    }

    override fun getAllWallets(id: Long): Array<KeyWalletDto> {
        return keyApi.getAllWalletsByApplication(id.toString())
    }

    override fun deleteWallet(applicationId: String, address: String) {
        // Delete from Open Key
        keyApi.deleteAllWalletsByApplicationAddress(applicationId, address)
        // Delete from Open State
        stateApi.deleteWallet(address, Blockchain.Ethereum)
    }

    override fun getAddressTransactionsByAddress(address: String): StateWalletTransactionDetail {
        return stateApi.getAddressTransactionsByAddress(address)
    }

    override fun getTransactionsByAddress(address: String): Array<TransactionDto> {
       return stateApi.getTransactionsByAddress(address)
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

    override fun exportPrivateKey(keyWalletDto: KeyWalletDto): String {
        return keyApi.exportPrivateKey(keyWalletDto)
    }
}