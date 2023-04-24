package io.openfuture.api.service

import io.openfuture.api.component.key.KeyApi
import io.openfuture.api.component.state.StateApi
import io.openfuture.api.component.web3.Web3Wrapper
import io.openfuture.api.controller.api.GenerateWalletWithMetadataRequest
import io.openfuture.api.controller.api.ImportWalletForUserRequest
import io.openfuture.api.controller.api.ImportWalletWithOrderRequest
import io.openfuture.api.domain.key.*
import io.openfuture.api.domain.state.*
import io.openfuture.api.domain.widget.PaymentWidgetResponse
import io.openfuture.api.entity.application.Application
import io.openfuture.api.entity.application.BlockchainType
import io.openfuture.api.entity.auth.User
import io.openfuture.api.entity.state.Blockchain
import io.openfuture.api.entity.state.Blockchain.Companion.getBlockchainBySymbol
import io.openfuture.keymanagementservice.dto.GenerateMultipleWalletForOrderRequest
import io.openfuture.keymanagementservice.dto.GenerateMultipleWalletForUserRequest
import org.springframework.stereotype.Service
import org.web3j.protocol.core.methods.response.TransactionReceipt
import java.math.BigInteger
import java.util.*

@Service
class DefaultWalletApiService(
    private val keyApi: KeyApi,
    private val stateApi: StateApi,
    private val web3: Web3Wrapper
) : WalletApiService {

    override fun generateWalletForOrder(walletApiCreateRequest: WalletApiCreateRequest, applicationId: Application, userId: String): Array<KeyWalletDto> {
        val keyWallets = keyApi.generateMultipleWalletsWithOrder(
            GenerateMultipleWalletForOrderRequest(
                applicationId.id.toString(),
                userId,
                walletApiCreateRequest.metadata.orderKey,
                walletApiCreateRequest.metadata.paymentCurrency
            )
        )

        val blockchains = extractAddresses(keyWallets, walletApiCreateRequest.metadata.test)

        val request = CreateStateWalletRequestMetadata(
            applicationId = applicationId.id.toString(),
            blockchains = blockchains,
            metadata = WalletMetaData(
                amount = walletApiCreateRequest.metadata.amount,
                orderKey = walletApiCreateRequest.metadata.orderKey,
                productCurrency = walletApiCreateRequest.metadata.productCurrency,
                source = walletApiCreateRequest.metadata.source,
                test = walletApiCreateRequest.metadata.test,
                metadata = walletApiCreateRequest.metadata.metadata
            ),
            webhook = applicationId.webHook.toString()
        )
        // Save webhook on open state
        stateApi.createWalletWithMetadata(request)

        return keyWallets
    }

    override fun processOrder(
        walletApiCreateRequest: WalletApiCreateRequest,
        application: Application,
        userId: String
    ): Array<KeyWalletDto> {
        return generateWalletForOrder(walletApiCreateRequest, application, userId)
    }

    override fun processUser(request: GenerateWalletWithMetadataRequest, application: Application): Array<KeyWalletDto> {
        val userId = application.user.id.toString()

        val keyWallets = keyApi.generateMultipleWalletsWithUser(
            GenerateMultipleWalletForUserRequest(application.id.toString(), userId, request.blockchains)
        )

        val blockchains = extractAddresses(keyWallets, request.test)

        val createStateRequest = CreateStateWithUserRequest(request.webhook, blockchains, application.id.toString(), userId, request.test, request.metadata)
        stateApi.createWallet(createStateRequest)

        return keyWallets
    }


    override fun saveWalletSDK(
        walletApiStateRequest: WalletApiStateRequest,
        application: Application,
        user: User
    ): Boolean {
        // Save Address on Open Key
        keyApi.importWallet(
            ImportKeyRequest(
                application.id.toString(),
                user.id.toString(),
                walletApiStateRequest.blockchain,
                walletApiStateRequest.address,
                walletApiStateRequest.encrypted
            )
        )

        // Save address on open state
        stateApi.createWallet(
            walletApiStateRequest.address,
            application.webHook!!,
            getBlockchainBySymbol(walletApiStateRequest.blockchain.getValue()),
            application.id.toString()
        )

        return true
    }

    override fun getOrderDetails(applicationId: String): List<StatePaymentDetail> {
        val keyWallets = keyApi.getAllWalletsByApplication(applicationId, Optional.empty(), Optional.empty())
        val orderWallets =  stateApi.getOrdersByApplication(applicationId)
        val result = mutableListOf<StatePaymentDetail>()

        orderWallets.forEach { o ->
            val bWallets = mutableListOf<BlockchainWallet>()
            val blockchainWallets = o.blockchains
            blockchainWallets.forEach { w ->
                val encrypted = keyWallets.find { k -> k.address.equals(w.address, true) }?.encrypted
                bWallets.add(BlockchainWallet(w.address, w.blockchain, w.rate, encrypted))
            }
            result.add(StatePaymentDetail(o.orderKey, o.amount, o.totalPaid, o.currency, bWallets))
        }
        return result
    }

    override fun getWallet(address: String, blockchainType: BlockchainType): WalletApiStateResponse {
        return stateApi.getWallet(address, getBlockchainBySymbol(blockchainType.getValue()))
    }

    override fun getWalletsByApplicationAndUser(applicationId: String, userId: String): Array<KeyWalletDto> {
        return keyApi.getAllWalletsByApplication(applicationId, Optional.of(userId), Optional.empty())
    }

    override fun getWalletsByApplicationAndOrder(applicationId: String, orderId: String): Array<KeyWalletDto> {
        return keyApi.getAllWalletsByApplication(applicationId, Optional.empty(), Optional.of(orderId))
    }

    override fun getNonce(address: String): BigInteger {
        return web3.getNonce(address)
    }

    override fun broadcastTransaction(signature: String, blockchainType: BlockchainType): TransactionReceipt {
        return web3.broadcastTransaction(signature)
    }

    override fun getAddressesByOrderKey(orderKey: String): PaymentWidgetResponse {
        return stateApi.getPaymentDetailByOrder(orderKey)
    }

    override fun importWalletForOrder(request: ImportWalletWithOrderRequest, application: Application): List<KeyWalletDto> {
        val wallets: List<KeyWalletDto> = request.wallets.map { walletIdentity ->
            keyApi.importWalletV2(
                ImportWalletOpenKeyRequest(
                    application.id.toString(),
                    application.user.id.toString(),
                    request.orderId,
                    walletIdentity.address,
                    walletIdentity.encryptedData,
                    walletIdentity.blockchainType
                )
            )
        }

        val stateRequest = CreateStateWalletRequestMetadata(
            webhook = request.webhook,
            applicationId = application.id.toString(),
            blockchains = extractAddresses(wallets.toTypedArray(), request.test),
            metadata = WalletMetaData(request.amount, request.orderId, request.orderCurrency, "order_sdk", request.test, request.metadata)
        )

        // Save webhook on open state
        stateApi.createWalletWithMetadata(stateRequest)

        return wallets
    }

    override fun importWalletForUser(request: ImportWalletForUserRequest, application: Application): List<KeyWalletDto> {
        val wallets: List<KeyWalletDto> = request.wallets.map { walletIdentity ->
            keyApi.importWalletV2(
                ImportWalletOpenKeyRequest(
                    application.id.toString(),
                    application.user.id.toString(),
                    request.userId,
                    walletIdentity.address,
                    walletIdentity.encryptedData,
                    walletIdentity.blockchainType
                )
            )
        }

        val stateRequest = CreateStateWithUserRequest(
            request.webhook,
            extractAddresses(wallets.toTypedArray(), request.test),
            application.id.toString(),
            request.userId,
            request.test,
            request.metadata
        )

        stateApi.createWallet(stateRequest)

        return wallets
    }

    private fun extractAddresses(keyWallets: Array<KeyWalletDto>, isTest: Boolean): MutableList<BlockchainData> {
        val blockchains = mutableListOf<BlockchainData>()

        for (keyWalletDto in keyWallets) {
            if (isTest && keyWalletDto.blockchain == "ETH") {
                blockchains.add(
                    BlockchainData(
                        keyWalletDto.address,
                        Blockchain.Goerli.getValue()
                    )
                )
            } else if (isTest && keyWalletDto.blockchain == "BNB") {
                blockchains.add(
                    BlockchainData(
                        keyWalletDto.address,
                        Blockchain.BinanceTestnet.getValue()
                    )
                )
            } else {
                when (keyWalletDto.blockchain) {
                    "ETH" -> {
                        blockchains.add(
                            BlockchainData(
                                keyWalletDto.address,
                                Blockchain.Ethereum.getValue()
                            )
                        )
                    }

                    "BTC" -> {
                        blockchains.add(
                            BlockchainData(
                                keyWalletDto.address,
                                Blockchain.Bitcoin.getValue()
                            )
                        )
                    }//todo: add trx
                    else -> {
                        blockchains.add(
                            BlockchainData(
                                keyWalletDto.address,
                                Blockchain.Binance.getValue()
                            )
                        )
                    }
                }
            }
        }
        return blockchains
    }

}