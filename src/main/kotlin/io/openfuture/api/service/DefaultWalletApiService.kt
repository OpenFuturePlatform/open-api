package io.openfuture.api.service

import io.openfuture.api.component.key.KeyApi
import io.openfuture.api.component.state.StateApi
import io.openfuture.api.component.web3.Web3Wrapper
import io.openfuture.api.domain.key.*
import io.openfuture.api.domain.state.*
import io.openfuture.api.domain.wallet.WalletType
import io.openfuture.api.domain.widget.PaymentWidgetResponse
import io.openfuture.api.entity.application.Application
import io.openfuture.api.entity.application.BlockchainType
import io.openfuture.api.entity.auth.User
import io.openfuture.api.entity.state.Blockchain
import io.openfuture.api.entity.state.Blockchain.Companion.getBlockchainBySymbol

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

    override fun generateWallet(
        walletApiCreateRequest: WalletApiCreateRequest,
        applicationId: Application,
        userId: String
    ): Array<KeyWalletDto> {
        // Generate address on open key
        val keyWallets = keyApi.generateMultipleWallets(
            CreateMultipleKeyRequest(
                applicationId.id.toString(),
                userId,
                walletApiCreateRequest.metadata.orderKey,
                walletApiCreateRequest.metadata.paymentCurrency
            )
        )

        val blockchains = mutableListOf<KeyWalletDto>()

        for (keyWalletDto in keyWallets) {
            if (walletApiCreateRequest.metadata.test && keyWalletDto.blockchain == "ETH") {
                blockchains.add(
                    KeyWalletDto(
                        keyWalletDto.address,
                        Blockchain.Ropsten.getValue(),
                        WalletType.CUSTODIAL.getValue(),
                        ""
                    )
                )
            } else if (walletApiCreateRequest.metadata.test && keyWalletDto.blockchain == "BNB") {
                blockchains.add(
                    KeyWalletDto(
                        keyWalletDto.address,
                        Blockchain.BinanceTestnet.getValue(),
                        WalletType.CUSTODIAL.getValue(),
                        ""
                    )
                )
            } else {
                when (keyWalletDto.blockchain) {
                    "ETH" -> {
                        blockchains.add(
                            KeyWalletDto(
                                keyWalletDto.address,
                                Blockchain.Ethereum.getValue(),
                                WalletType.CUSTODIAL.getValue(),
                                ""
                            )
                        )
                    }
                    "BTC" -> {
                        blockchains.add(
                            KeyWalletDto(
                                keyWalletDto.address,
                                Blockchain.Bitcoin.getValue(),
                                WalletType.CUSTODIAL.getValue(),
                                ""
                            )
                        )
                    }//todo: add trx
                    else -> {
                        blockchains.add(
                            KeyWalletDto(
                                keyWalletDto.address,
                                Blockchain.Binance.getValue(),
                                WalletType.CUSTODIAL.getValue(),
                                ""
                            )
                        )
                    }
                }
            }
        }

        val request = CreateStateWalletRequestMetadata(
            applicationId.webHook.toString(),
            applicationId.id.toString(),
            blockchains,
            WalletMetaData(
                walletApiCreateRequest.metadata.amount,
                walletApiCreateRequest.metadata.orderKey,
                walletApiCreateRequest.metadata.productCurrency,
                walletApiCreateRequest.metadata.source,
                walletApiCreateRequest.metadata.test
            )
        )
        // Save webhook on open state
        stateApi.createWalletWithMetadata(request)

        return keyWallets
    }

    override fun processWalletSDK(
        walletApiCreateRequest: WalletApiCreateRequest,
        application: Application,
        userId: String
    ): Array<KeyWalletDto> {

        return generateWallet(walletApiCreateRequest, application, userId)
    }

    override fun saveWalletSDK(
        walletApiStateRequest: WalletApiStateRequest,
        application: Application,
        user: User
    ): Boolean {
        println("WalletApiStateRequest :$walletApiStateRequest")
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

    override fun getOrderDetails(applicationId: String): Array<StateOrderDetail> {
        return stateApi.getOrderDetailsByApplication(applicationId)
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
}