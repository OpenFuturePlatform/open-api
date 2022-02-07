package io.openfuture.api.component.state

import io.openfuture.api.domain.key.WalletMetaDto
import io.openfuture.api.domain.state.StateWalletDto
import io.openfuture.api.domain.transaction.TransactionDto
import io.openfuture.api.entity.state.Blockchain

interface StateApi {

    fun createWallet(address: String, webHook: String, blockchain: Blockchain): StateWalletDto

    fun createWalletWithMetadata(address: String, webHook: String, blockchain: Blockchain, walletMetaDto: WalletMetaDto?): StateWalletDto

    fun deleteWallet(address: String, blockchain: Blockchain)

    fun getAddressTransactions(address: String) : Array<TransactionDto>
}
