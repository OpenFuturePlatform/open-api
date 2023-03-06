package io.openfuture.api.component.state

import io.openfuture.api.domain.state.WalletApiStateResponse
import io.openfuture.api.domain.state.*
import io.openfuture.api.domain.transaction.TransactionDto
import io.openfuture.api.domain.widget.PaymentWidgetResponse
import io.openfuture.api.entity.state.Blockchain

interface StateApi {

    fun createWallet(address: String, webHook: String, blockchain: Blockchain, applicationId: String): StateWalletDto

    fun createWalletWithMetadata(request: CreateStateWalletRequestMetadata): CreateStateWalletResponse

    fun updateWalletWithMetadata(request: UpdateStateWalletMetadata)

    fun deleteWallet(address: String, blockchain: Blockchain)

    fun getWallet(address: String, blockchain: Blockchain): WalletApiStateResponse

    fun getAddressTransactionsByAddress(address: String): StateWalletTransactionDetail

    fun getTransactionsByAddress(address: String): Array<TransactionDto>

    fun getPaymentDetailByOrder(orderKey: String): PaymentWidgetResponse

    fun getOrderDetailsByApplication(applicationId: String): Array<StateOrderDetail>

    fun createWallet(request: CreateStateWithUserRequest): AddWatchResponse
}
