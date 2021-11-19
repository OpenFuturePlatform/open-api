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
@RequestMapping("/oauth2/v1/wallet")
class WalletApiController(
    private val service: WalletApiService,
    private val applicationService: ApplicationService
) {

    @PreAuthorize(value = "hasAnyRole('ROLE_APPLICATION')")
    @PostMapping(path = ["/generate"], consumes = [MediaType.APPLICATION_FORM_URLENCODED_VALUE])
    fun generateWallet(@RequestParam paramMap: MutableMap<String, Any>, httpServletRequest: HttpServletRequest): KeyWalletDto? {
        return service.generateWallet(paramMap, httpServletRequest)
    }

    @PostMapping("/gen")
    fun generateWallet(@RequestBody walletApiCreateRequest: WalletApiCreateRequest, httpServletRequest: HttpServletRequest, @CurrentUser user: User): KeyWalletDto? {
        val accessKey = httpServletRequest.getHeader("X-API-KEY")
        val application = applicationService.getByAccessKey(accessKey)
        return service.generateWallet(walletApiCreateRequest, application, user)
    }
}