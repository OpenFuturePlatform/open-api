package io.openfuture.api.component.state

import io.openfuture.api.domain.state.CreateStateWalletRequestMetadata
import io.openfuture.api.domain.state.StateWalletDto
import io.openfuture.api.domain.state.StateWalletTransactionDetail
import io.openfuture.api.domain.transaction.TransactionDto
import io.openfuture.api.entity.state.Blockchain

interface StateApi {

    fun createWallet(address: String, webHook: String, blockchain: Blockchain): StateWalletDto

    fun createWalletWithMetadata(request: CreateStateWalletRequestMetadata)

    fun deleteWallet(address: String, blockchain: Blockchain)

    fun getAddressTransactionsByAddress(address: String) : StateWalletTransactionDetail

    fun getAddressTransactionsByOrder(orderKey: String) : StateWalletTransactionDetail
}
