package io.openfuture.api.controller.api

import com.fasterxml.jackson.annotation.JsonProperty
import io.openfuture.api.domain.key.ImportWalletRequest
import io.openfuture.api.domain.key.WalletApiCreateRequest
import io.openfuture.api.domain.key.WalletMetaDto
import io.openfuture.api.entity.application.BlockchainType
import io.openfuture.api.entity.auth.User
import io.openfuture.api.service.ApplicationService
import io.openfuture.api.service.ApplicationWalletService
import io.openfuture.api.service.WalletApiService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/public/api/v2/wallet")
class WalletTrackerController(
    private val walletApiService: WalletApiService,
    private val applicationService: ApplicationService,
    private val service: ApplicationWalletService
) {

    @PostMapping("user/generate")
    fun generateWalletForUser(request: GenerateWalletForUserRequest): Array<WalletResponse> {
        return emptyArray()
    }

    @PostMapping("user/import")
    fun importWalletForUser(request: ImportWalletForUserRequest): ImportWalletResponse {
        return ImportWalletResponse("Accepted")
    }

    @PostMapping("order/generate")
    fun generateWalletWithOrder(request: GenerateWalletWithOrderRequest, @RequestHeader("X-API-KEY") accessKey: String): List<WalletResponse> {
        val application = applicationService.getByAccessKey(accessKey)
        val walletApiCreateRequest = WalletApiCreateRequest(
            request.timestamp, WalletMetaDto(
                request.amount,
                request.orderId,
                request.blockchains,
                request.orderCurrency,
                "WOOCOMMERCE",
                request.test,
                false,
                request.masterPassword
            )
        )
        val walletSDK = walletApiService.processWalletSDK(walletApiCreateRequest, application, application.user)

        return walletSDK.map { WalletResponse(it.address, it.blockchain, it.walletType, it.encrypted) }
    }

    @PostMapping("order/import")
    fun importWalletWithOrder(request: ImportWalletWithOrderRequest): ImportWalletResponse {
        return ImportWalletResponse("Accepted")
    }

    @PostMapping("generate")
    fun generateWallet(request: GenerateWalletRequest): Array<WalletResponse> {
        return emptyArray()
    }

    @PostMapping("import")
    fun importWallet(request: io.openfuture.api.controller.api.ImportWalletRequest): ImportWalletResponse {
        return ImportWalletResponse("Accepted")
    }

    fun getAddressByUserId() {
        
    }

}

data class GenerateWalletForUserRequest(
    val blockchains: List<BlockchainType>,
    @JsonProperty("master_password")
    val masterPassword: String,
    var test: Boolean,
    var timestamp: String,
    @JsonProperty("user_id")
    val userId: String,
    var webhook: String
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

data class WalletResponse(
    val address: String,
    val blockchain: String,
    val walletType: String,
    val encrypted: String
)

data class ImportWalletForUserRequest(
    val userId: String,
    val encryptedData: String,
    val masterPassword: String,
    val webhook: String,
    var timestamp: String
)

data class ImportWalletWithOrderRequest(
    val orderId: String,
    var amount: String,
    var orderCurrency: String,//TODO: make enum
    val encryptedData: String,
    val masterPassword: String,
    val webhook: String,
    var timestamp: String
)

data class ImportWalletRequest(
    val uniqueId: String,
    val encryptedData: String,
    val masterPassword: String,
    val webhook: String,
    var timestamp: String,
    var metadata: Map<String, String>
)

data class ImportWalletResponse(
    val status: String
)
