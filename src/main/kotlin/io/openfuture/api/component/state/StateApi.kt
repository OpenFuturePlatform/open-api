package io.openfuture.api.component.state

import io.openfuture.api.domain.state.*
import io.openfuture.api.domain.transaction.TransactionDto
import io.openfuture.api.domain.widget.PaymentWidgetResponse
import io.openfuture.api.entity.state.Blockchain

interface StateApi {

    fun createWallet(address: String, webHook: String, blockchain: Blockchain): CreateStateWalletResponse

    fun createWalletWithMetadata(request: CreateStateWalletRequestMetadata): CreateStateWalletResponse

    fun deleteWallet(address: String, blockchain: Blockchain)

    fun getAddressTransactionsByAddress(address: String): StateWalletTransactionDetail

    fun getTransactionsByAddress(address: String): Array<TransactionDto>

    fun getPaymentDetailByOrder(orderKey: String): PaymentWidgetResponse
}
