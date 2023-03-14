package io.openfuture.api.controller.api

import com.fasterxml.jackson.annotation.JsonProperty
import io.openfuture.api.domain.key.*
import io.openfuture.api.entity.application.Application
import io.openfuture.api.entity.application.BlockchainType
import io.openfuture.api.entity.auth.User
import io.openfuture.api.service.ApplicationService
import io.openfuture.api.service.ApplicationWalletService
import io.openfuture.api.service.WalletApiService
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/v2/wallet")
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
    fun importWalletForUser(@RequestBody request: ImportWalletForUserRequest, @RequestHeader("X-API-KEY") accessKey: String): KeyWalletDto {
        val application = applicationService.getByAccessKey(accessKey)
        return walletApiService.importWalletForUser(request, application)
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
                metadata = request.metadata
            )
        )
        val walletSDK = walletApiService.processOrder(walletApiCreateRequest, application, application.user.id.toString())

        return walletSDK.map { KeyWalletDto(it.address, it.blockchain, it.walletType, it.encrypted) }
    }

    @PostMapping("order/import")
    fun importWalletWithOrder(@RequestBody request: ImportWalletWithOrderRequest, @RequestHeader("X-API-KEY") accessKey: String): KeyWalletDto {
        val application = applicationService.getByAccessKey(accessKey)
        return walletApiService.importWalletForOrder(request, application)
    }

    @GetMapping("order")
    fun getOrderWallets(@RequestParam("orderId") orderId: String, @RequestHeader("X-API-KEY") accessKey: String): Array<KeyWalletDto> {
        val application = applicationService.getByAccessKey(accessKey)
        return walletApiService.getWalletsByApplicationAndOrder(application.id.toString(), orderId)
    }

    @PostMapping("generate")
    fun generateWallet(@RequestBody request: GenerateWalletWithMetadataRequest, @RequestHeader("X-API-KEY") accessKey: String): Array<KeyWalletDto> {
        val application = applicationService.getByAccessKey(accessKey)
        return walletApiService.processUser(request, application)
    }

    @PostMapping("import")
    fun importWallet(@RequestBody request: ImportWalletRequest): KeyWalletDto {
        val application = applicationService.getByAccessKey(request.address)
        val importWalletForUserRequest = ImportWalletForUserRequest(
            request.address,
            request.blockchainType,
            request.encryptedData,
            request.metadata,
            application.user.id.toString()
        )
        return walletApiService.importWalletForUser(importWalletForUserRequest, application)
    }
}

data class GenerateWalletForUserRequest(
    @JsonProperty("blockchains")
    val blockchains: List<BlockchainType>,
    @JsonProperty("master_password")
    var metadata: Any,// TODO: Optional
    var test: Boolean,
    @JsonProperty("user_id")
    val userId: String
)

data class GenerateWalletWithOrderRequest(
    @JsonProperty("amount")
    val amount: String,
    @JsonProperty("blockchains")
    val blockchains: List<BlockchainType>,
    @JsonProperty("metadata")
    var metadata: Any? = null, //Optional
    var test: Boolean,
    @JsonProperty("order_currency")
    var orderCurrency: String,//TODO: make enum
    @JsonProperty("order_id")
    val orderId: String
)

data class GenerateWalletWithMetadataRequest(
    val blockchains: List<BlockchainType>,
    var metadata: Any? = null,
    var test: Boolean
)

data class ImportWalletForUserRequest(
    var address: String,
    val blockchainType: BlockchainType,
    val encryptedData: String,
    val metadata: Any?,
    val userId: String
)

data class ImportWalletWithOrderRequest(
    var address: String,
    var amount: String,
    val blockchainType: BlockchainType,
    val encryptedData: String,
    var orderCurrency: String,//TODO: make enum (USD, EUR, etc.)
    val orderId: String,
    val metadata: Any?
)

data class ImportWalletRequest(
    var address: String,
    val blockchainType: BlockchainType,
    val encryptedData: String,
    var metadata: Any?
)
