package io.openfuture.api.service

import io.openfuture.api.component.key.KeyApi
import io.openfuture.api.component.state.StateApi
import io.openfuture.api.domain.key.CreateKeyRequest
import io.openfuture.api.domain.key.GenerateWalletRequest
import io.openfuture.api.domain.key.KeyWalletDto
import io.openfuture.api.domain.key.WalletApiCreateRequest
import io.openfuture.api.entity.application.Application
import io.openfuture.api.entity.application.BlockchainType
import io.openfuture.api.entity.auth.User
import io.openfuture.api.entity.state.Blockchain
import io.openfuture.api.exception.NotFoundException
import io.openfuture.api.util.KeyGeneratorUtils
import org.springframework.stereotype.Service
import javax.servlet.http.HttpServletRequest

@Service
class DefaultWalletApiService(
    private val keyApi: KeyApi,
    private val stateApi: StateApi
) : WalletApiService{

    override fun generateWallet(
        walletApiCreateRequest: WalletApiCreateRequest,
        application: Application,
        user: User
    ): KeyWalletDto {
        // Generate address on open key
        val keyWalletDto  = keyApi.generateKey(CreateKeyRequest(application.id.toString(), user.id.toString(), walletApiCreateRequest.blockchain))

        // Save webhook on open state
        stateApi.createWallet(keyWalletDto.address, application.webHook.toString(), Blockchain.Ethereum, walletApiCreateRequest.metadata)

        return keyWalletDto
    }
}