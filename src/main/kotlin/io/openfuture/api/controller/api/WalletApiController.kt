package io.openfuture.api.controller.api

import io.openfuture.api.annotation.CurrentUser
import io.openfuture.api.domain.key.KeyWalletDto
import io.openfuture.api.domain.key.WalletApiCreateRequest
import io.openfuture.api.entity.auth.User
import io.openfuture.api.service.ApplicationService
import io.openfuture.api.service.WalletApiService
import org.springframework.http.MediaType
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("/public/api/v1/wallet")
class WalletApiController(
    private val service: WalletApiService,
    private val applicationService: ApplicationService
) {

    //@PreAuthorize(value = "hasAnyRole('ROLE_APPLICATION')")
    @PostMapping("/generate")
    fun generateWallet(
        @RequestBody walletApiCreateRequest: WalletApiCreateRequest,
        @RequestHeader("X-API-KEY") accessKey: String
    ): KeyWalletDto? {

        val application = applicationService.getByAccessKey(accessKey)

        return service.generateWallet(walletApiCreateRequest, application, application.user)
    }
}