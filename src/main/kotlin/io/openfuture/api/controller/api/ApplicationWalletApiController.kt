package io.openfuture.api.controller.api

import io.openfuture.api.annotation.CurrentUser
import io.openfuture.api.domain.key.GenerateWalletRequest
import io.openfuture.api.domain.key.ImportWalletRequest
import io.openfuture.api.domain.key.KeyWalletDto
import io.openfuture.api.domain.key.KeyWalletEncryptedDto
import io.openfuture.api.domain.state.StateSignRequest
import io.openfuture.api.domain.state.StateWalletTransactionDetail
import io.openfuture.api.entity.auth.User
import io.openfuture.api.service.ApplicationWalletService
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/api/application/wallet")
class ApplicationWalletApiController(
    private val service: ApplicationWalletService
) {

    @PostMapping
    fun generateWallet(@Valid @RequestBody request: GenerateWalletRequest, @CurrentUser user: User): KeyWalletDto =
        service.generateWallet(request, user.id.toString())

    @DeleteMapping
    fun delete(
        @RequestParam("applicationId") applicationId: String,
        @RequestParam("address") address: String
    ): Boolean {
        service.deleteWallet(applicationId, address)
        return true
    }

    @GetMapping("/{applicationId}")
    fun getAll(@PathVariable("applicationId") applicationId: Long): Array<KeyWalletDto> {
        return service.getAllWallets(applicationId)
    }

    @PostMapping("/sign/address/{address}")
    fun generateSignature(
        @PathVariable("address") address: String,
        @Valid @RequestBody request: StateSignRequest
    ): String {
        return service.generateSignature(address, request)
    }

    @PostMapping("/export/private")
    fun exportPrivateKey(@Valid @RequestBody request: KeyWalletDto, @CurrentUser user: User): String =
        service.exportPrivateKey(request)

    @PostMapping("/import")
    fun importWallet(@Valid @RequestBody request: ImportWalletRequest, @CurrentUser user: User){
        service.importWallet(request, user.id.toString())
    }

    @GetMapping("/address/{address}")
    fun getAll(@PathVariable("address") address: String): StateWalletTransactionDetail {
        return service.getAddressTransactionsByAddress(address)
    }

}