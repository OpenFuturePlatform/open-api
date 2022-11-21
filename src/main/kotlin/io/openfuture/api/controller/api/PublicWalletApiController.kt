package io.openfuture.api.controller.api

import io.openfuture.api.domain.key.KeyWalletDto
import io.openfuture.api.domain.key.WalletApiCreateRequest
import io.openfuture.api.domain.state.WalletApiStateRequest
import io.openfuture.api.domain.state.WalletApiStateResponse
import io.openfuture.api.service.ApplicationService
import io.openfuture.api.service.WalletApiService
import org.springframework.web.bind.annotation.*
import org.web3j.protocol.core.methods.response.TransactionReceipt


@RestController
@RequestMapping("/public/api/v1/wallet")
class PublicWalletApiController(
    private val service: WalletApiService,
    private val applicationService: ApplicationService
) {

    //@PreAuthorize(value = "hasAnyRole('ROLE_APPLICATION')")
    @PostMapping("/process")
    fun generateWallet(
        @RequestBody walletApiCreateRequest: WalletApiCreateRequest,
        @RequestHeader("X-API-KEY") accessKey: String
    ): Array<KeyWalletDto> {
        val application = applicationService.getByAccessKey(accessKey)
        return service.processWalletSDK(walletApiCreateRequest, application, application.user)
    }

    @PostMapping("/save")
    fun saveWallet(
        @RequestBody walletApiStateRequest: WalletApiStateRequest,
        @RequestHeader("OPEN-API-KEY") accessKey: String
    ): Boolean {
        val application = applicationService.getByAccessKey(accessKey)
        return service.saveWalletSDK(walletApiStateRequest, application, application.user)
    }

    @PostMapping("/fetch")
    fun getWallet(
        @RequestBody walletApiStateRequest: WalletApiStateRequest
    ): WalletApiStateResponse {
        return service.getWallet(walletApiStateRequest.address, walletApiStateRequest.blockchain)
    }

    @PostMapping("/broadcast")
    fun broadcastTransaction(
        @RequestBody walletApiStateRequest: WalletApiStateRequest
    ): TransactionReceipt {
        return service.broadcastTransaction(walletApiStateRequest.address, walletApiStateRequest.blockchain)
    }
}