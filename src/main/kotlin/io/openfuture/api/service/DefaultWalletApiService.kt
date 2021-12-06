package io.openfuture.api.service

import io.openfuture.api.domain.key.GenerateWalletRequest
import io.openfuture.api.domain.key.KeyWalletDto
import io.openfuture.api.domain.key.WalletApiCreateRequest
import io.openfuture.api.entity.application.Application
import io.openfuture.api.entity.application.BlockchainType
import io.openfuture.api.entity.auth.User
import io.openfuture.api.exception.NotFoundException
import io.openfuture.api.util.KeyGeneratorUtils
import org.springframework.stereotype.Service
import javax.servlet.http.HttpServletRequest

@Service
class DefaultWalletApiService(
    private val applicationWalletService: ApplicationWalletService
) : WalletApiService{

    override fun generateWallet(
        walletApiCreateRequest: WalletApiCreateRequest,
        application: Application,
        user: User
    ): KeyWalletDto {
        return applicationWalletService.generateWallet(
            GenerateWalletRequest(application.id.toString(), application.webHook!!, walletApiCreateRequest.blockchain),
            user
        )
    }
}