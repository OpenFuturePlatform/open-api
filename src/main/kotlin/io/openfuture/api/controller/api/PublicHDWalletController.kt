package io.openfuture.api.controller.api

import io.openfuture.api.domain.key.*
import io.openfuture.api.entity.application.BlockchainType
import io.openfuture.api.service.ApplicationService
import io.openfuture.api.service.ApplicationWalletService
import io.openfuture.api.service.WalletApiService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("public/api/v2/wallet/")
class WalletTrackerController(
    private val walletApiService: WalletApiService,
    private val applicationService: ApplicationService,
    private val service: ApplicationWalletService
) {

    @PostMapping("user/generate")
    fun generateWalletForUser(
        @RequestBody request: GenerateWalletForUserRequest,
        @RequestHeader("X-API-KEY") accessKey: String
    ): List<KeyWalletDto> {
        val application = applicationService.getByAccessKey(accessKey)
        val blockchains = mutableListOf<KeyWalletDto>()
        for (blockchainType in request.blockchains) {
            val generateWalletRequest = GenerateWalletRequest(application.id.toString(), blockchainType, request.webhook)
            blockchains.add(service.generateWallet(generateWalletRequest, request.userId))
        }
        return blockchains
    }

    @PostMapping("user/import")
    fun importWalletForUser(
        @RequestBody request: ImportWalletForUserRequest,
        @RequestHeader("X-API-KEY") accessKey: String
    ): List<KeyWalletDto> {
        val application = applicationService.getByAccessKey(accessKey)
        return walletApiService.importWalletForUser(request, application)
    }

    @GetMapping("user")
    fun getUserWallets(
        @RequestParam("userId") userId: String,
        @RequestHeader("X-API-KEY") accessKey: String
    ): Array<KeyWalletDto> {
        val application = applicationService.getByAccessKey(accessKey)
        return walletApiService.getWalletsByApplicationAndUser(application.id.toString(), userId)
    }

    @PostMapping("order/generate")
    fun generateWalletWithOrder(
        @RequestBody request: GenerateWalletWithOrderRequest,
        @RequestHeader("X-API-KEY") accessKey: String
    ): List<KeyWalletDto> {
        val application = applicationService.getByAccessKey(accessKey)
        val walletApiCreateRequest = WalletApiCreateRequest(
            WalletOrderData(
                amount = request.amount,
                orderKey = request.orderId,
                paymentCurrency = request.blockchains,
                productCurrency = request.orderCurrency,
                source = "ORDER_SDK",
                test = request.test,
                clientManaged = false,
                metadata = request.metadata
            )
        )
        val walletSDK =
            walletApiService.processOrder(walletApiCreateRequest, application, application.user.id.toString())

        return walletSDK.map { KeyWalletDto(it.address, it.blockchain, it.walletType, it.encrypted) }
    }

    @PostMapping("order/import")
    fun importWalletWithOrder(
        @RequestBody request: ImportWalletWithOrderRequest,
        @RequestHeader("X-API-KEY") accessKey: String
    ): List<KeyWalletDto> {
        val application = applicationService.getByAccessKey(accessKey)
        return walletApiService.importWalletForOrder(request, application)
    }

    @GetMapping("order")
    fun getOrderWallets(
        @RequestParam("orderId") orderId: String,
        @RequestHeader("X-API-KEY") accessKey: String
    ): Array<KeyWalletDto> {
        val application = applicationService.getByAccessKey(accessKey)
        return walletApiService.getWalletsByApplicationAndOrder(application.id.toString(), orderId)
    }

    @PostMapping("generate")
    fun generateWallet(
        @RequestBody request: GenerateWalletWithMetadataRequest,
        @RequestHeader("X-API-KEY") accessKey: String
    ): Array<KeyWalletDto> {
        val application = applicationService.getByAccessKey(accessKey)
        //the implementation the same as the import wallets for user, except that userId taken from application.userId
        return walletApiService.processUser(request, application)
    }

    @PostMapping("import")
    fun importWallet(
        @RequestBody request: ImportWalletRequest,
        @RequestHeader("X-API-KEY") accessKey: String
    ): List<KeyWalletDto> {
        val application = applicationService.getByAccessKey(accessKey)

        //the implementation the same as the import wallets for user, except that userId taken from application.userId
        val importWalletForUserRequest = ImportWalletForUserRequest(
            request.metadata,
            request.test,
            application.user.id.toString(),
            request.wallets,
            request.webhook
        )

        return walletApiService.importWalletForUser(importWalletForUserRequest, application)
    }
}

data class GenerateWalletForUserRequest(
    val blockchains: List<BlockchainType>,
    var metadata: Any,// TODO: Optional
    var test: Boolean,
    val userId: String,
    val webhook: String
)

data class GenerateWalletWithOrderRequest(
    val amount: String,
    val blockchains: List<BlockchainType>,
    var metadata: Any? = null, //Optional
    var orderCurrency: String,//TODO: make enum
    val orderId: String,
    var test: Boolean,
    val webhook: String
)

data class GenerateWalletWithMetadataRequest(
    val blockchains: List<BlockchainType>,
    var metadata: Any? = null,
    var test: Boolean,
    val webhook: String
)

data class ImportWalletForUserRequest(
    val metadata: Any?,
    val test: Boolean,
    val userId: String,
    val wallets: List<WalletIdentity>,
    val webhook: String
)

data class ImportWalletWithOrderRequest(
    var amount: String,
    val metadata: Any?,
    var orderCurrency: String,//TODO: make enum (USD, EUR, etc.)
    val orderId: String,
    val test: Boolean,
    val wallets: List<WalletIdentity>,
    val webhook: String
)

data class ImportWalletRequest(
    var metadata: Any?,
    val test: Boolean,
    val wallets: List<WalletIdentity>,
    val webhook: String
)

data class WalletIdentity(
    var address: String,
    var blockchainType: BlockchainType,
    var encryptedData: String
)
