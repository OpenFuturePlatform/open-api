package io.openfuture.api.controller.api

import io.openfuture.api.domain.key.GenerateWalletRequest
import io.openfuture.api.domain.key.KeyWalletDto
import io.openfuture.api.service.ApplicationWalletService
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/api/application/wallet")
class ApplicationWalletApiController(
    private val service: ApplicationWalletService
) {

    @PostMapping
    fun generateWallet(@Valid @RequestBody request: GenerateWalletRequest): KeyWalletDto =
        service.generateWallet(request)

    @DeleteMapping
    fun delete(@RequestParam("applicationId") applicationId: String, @RequestParam("address") address: String): Boolean {
        service.deleteWallet(applicationId, address)
        return true
    }

    @GetMapping("/{applicationId}")
    fun getAll(@PathVariable("applicationId") applicationId: Long): Array<KeyWalletDto> {
        return service.getAllWallets(applicationId)
    }

}