package io.openfuture.api.service

import io.openfuture.api.component.key.KeyApi
import io.openfuture.api.component.state.StateApi
import io.openfuture.api.domain.key.*
import io.openfuture.api.domain.state.CreateStateWalletRequestMetadata
import io.openfuture.api.domain.state.WalletMetaData
import io.openfuture.api.domain.widget.PaymentWidgetResponse
import io.openfuture.api.entity.application.Application
import io.openfuture.api.entity.application.BlockchainType
import io.openfuture.api.entity.auth.User
import io.openfuture.api.entity.state.Blockchain
import io.openfuture.api.exception.NotFoundException
import io.openfuture.api.util.KeyGeneratorUtils

import io.openfuture.api.util.get7hFromDate
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.time.LocalDateTime
import javax.servlet.http.HttpServletRequest

@Service
class DefaultWalletApiService(
    private val keyApi: KeyApi,
    private val stateApi: StateApi
) : WalletApiService {

    override fun generateWallet(
        walletApiCreateRequest: WalletApiCreateRequest,
        application: Application,
        user: User
    ): Array<KeyWalletDto> {
        // Generate address on open key
        val keyWallets = keyApi.generateMultipleKey(
            CreateMultipleKeyRequest(
                application.id.toString(),
                user.id.toString(),
                walletApiCreateRequest.metadata?.orderKey!!,
                walletApiCreateRequest.metadata?.paymentCurrency!!
            )
        )

        val blockchains = mutableListOf<KeyWalletDto>()

        for (keyWalletDto in keyWallets) {
            if (walletApiCreateRequest.metadata?.test == true) {
                blockchains.add(KeyWalletDto(keyWalletDto.address, Blockchain.Ropsten.getValue()))
            } else {
                when (keyWalletDto.blockchain) {
                    "ETH" -> {
                        blockchains.add(KeyWalletDto(keyWalletDto.address, Blockchain.Ethereum.getValue()))
                    }
                    "BTC" -> {
                        blockchains.add(KeyWalletDto(keyWalletDto.address, Blockchain.Bitcoin.getValue()))
                    }
                    else -> {
                        blockchains.add(KeyWalletDto(keyWalletDto.address, Blockchain.Binance.getValue()))
                    }
                }
            }
        }

        val request = CreateStateWalletRequestMetadata(
            application.webHook.toString(),
            blockchains,
            WalletMetaData(
                walletApiCreateRequest.metadata!!.amount,
                walletApiCreateRequest.metadata!!.orderId,
                walletApiCreateRequest.metadata!!.orderKey,
                walletApiCreateRequest.metadata!!.productCurrency,
                walletApiCreateRequest.metadata!!.source,
                walletApiCreateRequest.metadata!!.test
            )
        )
        // Save webhook on open state
        stateApi.createWalletWithMetadata(request)

        return keyWallets
    }

    override fun getAddressesByOrderKey(orderKey: String): PaymentWidgetResponse {
        return stateApi.getPaymentDetailByOrder(orderKey)
    }
}