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
    private val applicationWalletService: ApplicationWalletService,
    private val applicationService: ApplicationService
) : WalletApiService{

    override fun generateWallet(
        walletApiCreateRequest: WalletApiCreateRequest,
        application: Application,
        user: User
    ): KeyWalletDto {
        return applicationWalletService.generateWallet(GenerateWalletRequest(application.id.toString(), application.webHook!!, walletApiCreateRequest.blockchain), user)
    }

    override fun generateWallet(
        paramMap: MutableMap<String, Any>,
        httpServletRequest: HttpServletRequest
    ): KeyWalletDto {
        val accessKey = httpServletRequest.getHeader("X-API-KEY")
        val signature = httpServletRequest.getHeader("X-API-SIGNATURE")

        val joinedParams = paramMap.entries.joinToString(separator = "&") { (key, value) ->
            "${key}=${value}"
        }
        val application = applicationService.getByAccessKey(accessKey)

        val hmacSha256 = KeyGeneratorUtils.calcHmacSha256(application.apiSecretKey, joinedParams)


        if (hmacSha256 == signature){
            return applicationWalletService.generateWallet(GenerateWalletRequest(application.id.toString(), application.webHook!!, BlockchainType.valueOf(
                paramMap["blockchain"].toString())), application.user)
        }
        throw NotFoundException("Signature does not match")
    }
}