package io.openfuture.api.controller.api

import io.openfuture.api.domain.key.KeyWalletDto
import io.openfuture.api.service.OauthWalletService
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("/oauth2/v1/wallet")
class WalletApiController(
    private val service: OauthWalletService
) {

    @PostMapping(path = ["/generate"], consumes = [MediaType.APPLICATION_FORM_URLENCODED_VALUE])
    fun generateWallet(@RequestParam paramMap: MutableMap<String, Any>, httpServletRequest: HttpServletRequest): KeyWalletDto? {
        return service.generateWallet(paramMap, httpServletRequest)
    }
}