package io.openfuture.api.controller.api

import io.openfuture.api.domain.key.KeyWalletDto
import io.openfuture.api.domain.key.WalletApiCreateRequest
import io.openfuture.api.service.ApplicationService
import io.openfuture.api.service.WalletApiService
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/public/api/v1/wallet")
class PublicWalletApiController(
    private val service: WalletApiService,
    private val applicationService: ApplicationService
) {

    //@PreAuthorize(value = "hasAnyRole('ROLE_APPLICATION')")
    @PostMapping("/generate")
    fun generateWallet(
        @RequestBody walletApiCreateRequest: WalletApiCreateRequest,
        @RequestHeader("X-API-KEY") accessKey: String
    ): Array<KeyWalletDto> {

        val application = applicationService.getByAccessKey(accessKey)

        return service.generateWallet(walletApiCreateRequest, application, application.user)
    }
}