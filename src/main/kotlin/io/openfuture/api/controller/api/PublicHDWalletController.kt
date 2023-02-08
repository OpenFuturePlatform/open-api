package io.openfuture.api.controller.api

import io.openfuture.api.domain.key.ImportWalletRequest
import io.openfuture.api.entity.application.BlockchainType
import io.openfuture.api.service.DefaultApplicationWalletService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/public/api/v2/wallet")
class WalletTrackerController(
    val walletService: DefaultApplicationWalletService
) {

    @PostMapping("user/generate")
    fun generateWalletForUser(request: GenerateWalletForUserRequest): Array<WallerResponse> {
        return emptyArray()
    }

    @PostMapping("user/import")
    fun importWalletForUser(request: ImportWalletForUserRequest): ImportWalletResponse {
        return ImportWalletResponse("Accepted")
    }

    @PostMapping("order/generate")
    fun generateWalletWithOrder(request: GenerateWalletWithOrderRequest): Array<WallerResponse> {
        return emptyArray()
    }

    @PostMapping("order/import")
    fun importWalletWithOrder(request: ImportWalletWithOrderRequest): ImportWalletResponse {
        return ImportWalletResponse("Accepted")
    }

    @PostMapping("generate")
    fun generateWallet(request: GenerateWalletRequest): Array<WallerResponse> {
        return emptyArray()
    }

    @PostMapping("import")
    fun importWallet(request: ImportWalletRequest): ImportWalletResponse {
        return ImportWalletResponse("Accepted")
    }

}

data class GenerateWalletForUserRequest(
    val userId: String,
    var webhook: String,
    var test: Boolean,
    val blockchains: List<BlockchainType>,
    val masterPassword: String,
    var timestamp: String
)

data class GenerateWalletWithOrderRequest(
    val orderId: String,
    var amount: String,
    var orderCurrency: String,//TODO: make enum
    var test: Boolean,
    var webhook: String,
    val blockchains: List<BlockchainType>,
    val masterPassword: String,
    var timestamp: String
)

data class GenerateWalletRequest(
    val uniqueId: String,
    var test: Boolean,
    var webhook: String,
    val blockchains: List<BlockchainType>,
    val masterPassword: String,
    var timestamp: String,
    var metadata: Map<String, String>
)

data class WallerResponse(
    val address: String,
    val blockchain: String,
    val walletType: String,
    val encrypted: String
)

data class ImportWalletForUserRequest(
    val userId: String,
    val encryptedPrivateKey: String,
    val masterPassword: String,
    val webhook: String,
    var timestamp: String
)

data class ImportWalletWithOrderRequest(
    val orderId: String,
    var amount: String,
    var orderCurrency: String,//TODO: make enum
    val encryptedPrivateKey: String,
    val masterPassword: String,
    val webhook: String,
    var timestamp: String
)

data class ImportWalletRequest(
    val uniqueId: String,
    val encryptedPrivateKey: String,
    val masterPassword: String,
    val webhook: String,
    var timestamp: String,
    var metadata: Map<String, String>
)

data class ImportWalletResponse(
    val status: String
)
