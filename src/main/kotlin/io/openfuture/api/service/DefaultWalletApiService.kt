package io.openfuture.api.service

import io.openfuture.api.component.key.KeyApi
import io.openfuture.api.component.state.StateApi
import io.openfuture.api.domain.key.*
import io.openfuture.api.domain.state.CreateStateWalletRequestMetadata
import io.openfuture.api.domain.widget.PaymentWidgetResponse
import io.openfuture.api.entity.application.Application
import io.openfuture.api.entity.application.BlockchainType
import io.openfuture.api.entity.auth.User
import io.openfuture.api.entity.state.Blockchain
import io.openfuture.api.exception.NotFoundException
import io.openfuture.api.util.KeyGeneratorUtils
import io.openfuture.api.util.get7hFromCurrent
import org.springframework.stereotype.Service
import java.math.BigDecimal
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

        /*for (keyWalletDto in keyWallets) {
            val request = CreateStateWalletRequestMetadata(
                keyWalletDto.address,
                application.webHook.toString(),
                if (walletApiCreateRequest.metadata?.test == true) Blockchain.Ropsten else Blockchain.Ethereum,
                walletApiCreateRequest.metadata
            )
            // Save webhook on open state
            stateApi.createWalletWithMetadata(request)
        }*/

        return keyWallets
    }

    override fun getAddressesByOrderKey(orderKey: String): PaymentWidgetResponse {
        val wallets = keyApi.getAllKeysByOrderKey(orderKey)
        return PaymentWidgetResponse(get7hFromCurrent().toString(), BigDecimal.ZERO, wallets.toList())
    }
}