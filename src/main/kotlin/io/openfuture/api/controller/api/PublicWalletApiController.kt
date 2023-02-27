package io.openfuture.api.controller.api

import io.openfuture.api.domain.key.KeyWalletDto
import io.openfuture.api.domain.key.KeyWalletEncryptedDto
import io.openfuture.api.domain.key.WalletApiCreateRequest
import io.openfuture.api.domain.state.StateOrderDetail
import io.openfuture.api.domain.state.WalletApiStateRequest
import io.openfuture.api.domain.state.WalletApiStateResponse
import io.openfuture.api.service.ApplicationService
import io.openfuture.api.service.ApplicationWalletService
import io.openfuture.api.service.WalletApiService
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import org.web3j.protocol.core.methods.response.TransactionReceipt


@RestController
@RequestMapping("/public/api/v1/wallet")
@PreAuthorize(value = "hasAnyRole('ROLE_APPLICATION')")
class PublicWalletApiController(
    private val walletApiService: WalletApiService,
    private val applicationService: ApplicationService,
    private val applicationWalletService: ApplicationWalletService
) {

    @PostMapping("/process")
    fun generateWallet(@RequestBody request: WalletApiCreateRequest, @RequestHeader("X-API-KEY") accessKey: String): Array<KeyWalletDto> {
        val application = applicationService.getByAccessKey(accessKey)
        return walletApiService.processWalletSDK(request, application, application.user.id.toString())
    }

    @GetMapping
    fun getWallets(@RequestHeader("X-API-KEY") accessKey: String): Array<KeyWalletDto> {
        val application = applicationService.getByAccessKey(accessKey)
        return applicationWalletService.getAllWallets(application.id)
    }

    @GetMapping("/details")
    fun getWalletDetails(@RequestHeader("X-API-KEY") accessKey: String): Array<StateOrderDetail> {
        val application = applicationService.getByAccessKey(accessKey)
        return walletApiService.getOrderDetails(application.id.toString())
    }

    @PostMapping("/save")
    fun saveWallet(@RequestBody request: WalletApiStateRequest, @RequestHeader("X-API-KEY") accessKey: String): Boolean {
        val application = applicationService.getByAccessKey(accessKey)
        return walletApiService.saveWalletSDK(request, application, application.user)
    }

    @PostMapping("/fetch")
    fun getWallet(@RequestBody request: WalletApiStateRequest): WalletApiStateResponse {
        return walletApiService.getWallet(request.address, request.blockchain)
    }

    @PostMapping("/broadcast")
    fun broadcastTransaction(@RequestBody request: WalletApiStateRequest): TransactionReceipt {
        return walletApiService.broadcastTransaction(request.address, request.blockchain)
    }
}