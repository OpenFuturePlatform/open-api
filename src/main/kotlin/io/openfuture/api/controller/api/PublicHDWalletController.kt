package io.openfuture.api.controller.api

import com.fasterxml.jackson.annotation.JsonProperty
import io.openfuture.api.domain.key.*
import io.openfuture.api.domain.key.ImportWalletRequest
import io.openfuture.api.entity.application.BlockchainType
import io.openfuture.api.entity.auth.User
import io.openfuture.api.service.ApplicationService
import io.openfuture.api.service.ApplicationWalletService
import io.openfuture.api.service.WalletApiService
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/public/api/v2/wallet")
class WalletTrackerController(
    private val walletApiService: WalletApiService,
    private val applicationService: ApplicationService,
    private val service: ApplicationWalletService
) {

    @PostMapping("user/generate")
    fun generateWalletForUser(@RequestBody request: GenerateWalletForUserRequest, @RequestHeader("X-API-KEY") accessKey: String): List<KeyWalletDto> {
        val application = applicationService.getByAccessKey(accessKey)
        val blockchains = mutableListOf<KeyWalletDto>()
        for (blockchainType in request.blockchains){
            val generateWalletRequest = GenerateWalletRequest(
                applicationId = application.id.toString(),
                webHook = application.webHook!!,
                blockchainType = blockchainType
            )
            blockchains.add(service.generateWallet(generateWalletRequest, request.userId))
        }
        return blockchains
    }

    @PostMapping("user/import")
    fun importWalletForUser(@RequestBody request: ImportWalletForUserRequest): ImportWalletResponse {
        return ImportWalletResponse("Accepted")
    }

    @GetMapping("user")
    fun getUserWallets(@RequestParam("userId") userId: String, @RequestHeader("X-API-KEY") accessKey: String): Array<KeyWalletDto> {
        val application = applicationService.getByAccessKey(accessKey)
        return walletApiService.getWalletsByApplicationAndUser(application.id.toString(), userId)
    }

    @PostMapping("order/generate")
    fun generateWalletWithOrder(@RequestBody request: GenerateWalletWithOrderRequest, @RequestHeader("X-API-KEY") accessKey: String): List<KeyWalletDto> {
        val application = applicationService.getByAccessKey(accessKey)
        val walletApiCreateRequest = WalletApiCreateRequest(
            WalletMetaDto(
                amount = request.amount,
                orderKey = request.orderId,
                paymentCurrency = request.blockchains,
                productCurrency = request.orderCurrency,
                source = "ORDER_SDK",
                test = false,
                clientManaged = false,
                clientPassword = request.masterPassword!!
            )
        )
        val walletSDK = walletApiService.processWalletSDK(walletApiCreateRequest, application, application.user.id.toString())

        return walletSDK.map { KeyWalletDto(it.address, it.blockchain, it.walletType, it.encrypted) }
    }

    @PostMapping("order/import")
    fun importWalletWithOrder(@RequestBody request: ImportWalletWithOrderRequest): ImportWalletResponse {
        return ImportWalletResponse("Accepted")
    }

    @GetMapping("order")
    fun getOrderWallets(@RequestParam("orderId") orderId: String, @RequestHeader("X-API-KEY") accessKey: String): Array<KeyWalletDto> {
        val application = applicationService.getByAccessKey(accessKey)
        return walletApiService.getWalletsByApplicationAndOrder(application.id.toString(), orderId)
    }

    @PostMapping("import")
    fun importWallet(@RequestBody request: io.openfuture.api.controller.api.ImportWalletRequest): ImportWalletResponse {
        return ImportWalletResponse("Accepted")
    }
}

data class GenerateWalletForUserRequest(
    @JsonProperty("blockchains")
    val blockchains: List<BlockchainType>,
    @JsonProperty("master_password")
    val masterPassword: String,
    var test: Boolean,
    @JsonProperty("user_id")
    val userId: String,
    var webhook: String
)

data class GenerateWalletWithOrderRequest(
    @JsonProperty("amount")
    val amount: String,
    @JsonProperty("blockchains")
    val blockchains: List<BlockchainType>,
    @JsonProperty("master_password")
    val masterPassword: String?,
    @JsonProperty("order_currency")
    var orderCurrency: String,//TODO: make enum
    @JsonProperty("order_id")
    val orderId: String,
    @JsonProperty("webhook")
    var webhook: String?
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
