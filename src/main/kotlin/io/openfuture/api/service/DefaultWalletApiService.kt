package io.openfuture.api.service

import io.openfuture.api.component.key.KeyApi
import io.openfuture.api.component.state.StateApi
import io.openfuture.api.component.web3.Web3Wrapper
import io.openfuture.api.domain.key.*
import io.openfuture.api.domain.state.*
import io.openfuture.api.domain.widget.PaymentWidgetResponse
import io.openfuture.api.entity.application.Application
import io.openfuture.api.entity.application.BlockchainType
import io.openfuture.api.entity.auth.User
import io.openfuture.api.entity.state.Blockchain

import org.springframework.stereotype.Service
import org.web3j.protocol.core.methods.response.TransactionReceipt
import java.math.BigInteger

@Service
class DefaultWalletApiService(
    private val keyApi: KeyApi,
    private val stateApi: StateApi,
    private val web3: Web3Wrapper
) : WalletApiService {

    override fun generateWallet(
        walletApiCreateRequest: WalletApiCreateRequest,
        application: Application,
        user: User
    ): Array<KeyWalletDto> {
        // Generate address on open key
        val keyWallets = keyApi.generateMultipleWallets(
            CreateMultipleKeyRequest(
                application.id.toString(),
                user.id.toString(),
                walletApiCreateRequest.metadata.orderKey,
                walletApiCreateRequest.metadata.paymentCurrency
            )
        )

        val blockchains = mutableListOf<KeyWalletDto>()

        for (keyWalletDto in keyWallets) {
            if (walletApiCreateRequest.metadata.test && keyWalletDto.blockchain == "ETH") {
                blockchains.add(KeyWalletDto(keyWalletDto.address, Blockchain.Ropsten.getValue(), "CUSTODIAL"))
            } else if (walletApiCreateRequest.metadata.test && keyWalletDto.blockchain == "BNB") {
                blockchains.add(
                    KeyWalletDto(
                        keyWalletDto.address,
                        Blockchain.BinanceTestnetBlockchain.getValue(),
                        "CUSTODIAL"
                    )
                )
            } else {
                when (keyWalletDto.blockchain) {
                    "ETH" -> {
                        blockchains.add(KeyWalletDto(keyWalletDto.address, Blockchain.Ethereum.getValue(), "CUSTODIAL"))
                    }
                    "BTC" -> {
                        blockchains.add(KeyWalletDto(keyWalletDto.address, Blockchain.Bitcoin.getValue(), "CUSTODIAL"))
                    }
                    else -> {
                        blockchains.add(KeyWalletDto(keyWalletDto.address, Blockchain.Binance.getValue(), "CUSTODIAL"))
                    }
                }
            }
        }

        val request = CreateStateWalletRequestMetadata(
            application.webHook.toString(),
            application.id.toString(),
            blockchains,
            WalletMetaData(
                walletApiCreateRequest.metadata.amount,
                walletApiCreateRequest.metadata.orderKey,
                walletApiCreateRequest.metadata.productCurrency,
                walletApiCreateRequest.metadata.source,
                walletApiCreateRequest.metadata.test,
                walletApiCreateRequest.metadata.clientManaged
            )
        )
        // Save webhook on open state
        stateApi.createWalletWithMetadata(request)

        return keyWallets
    }

    override fun processWalletSDK(
        walletApiCreateRequest: WalletApiCreateRequest,
        application: Application,
        user: User
    ): Array<KeyWalletDto> {

        if (!walletApiCreateRequest.metadata.clientManaged) { // SHOULD BE USED CUSTODIAL WALLET
            return generateWallet(walletApiCreateRequest, application, user)
        }

        val keyWallets = keyApi.updateWallets(
            CreateMultipleKeyRequest(
                application.id.toString(),
                user.id.toString(),
                walletApiCreateRequest.metadata.orderKey,
                walletApiCreateRequest.metadata.paymentCurrency
            )
        )

        val request = UpdateStateWalletMetadata(
            application.webHook.toString(),
            application.id.toString(),
            WalletMetaData(
                walletApiCreateRequest.metadata.amount,
                walletApiCreateRequest.metadata.orderKey,
                walletApiCreateRequest.metadata.productCurrency,
                walletApiCreateRequest.metadata.source,
                walletApiCreateRequest.metadata.test,
                walletApiCreateRequest.metadata.clientManaged
            )
        )
        stateApi.updateWalletWithMetadata(request)

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
        return true
    }

    override fun getWallet(address: String, blockchainType: BlockchainType): WalletApiStateResponse {
        return stateApi.getWallet(address, Blockchain.getBlockchainBySymbol(blockchainType.getValue()))
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