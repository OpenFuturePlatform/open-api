package io.openfuture.api.component.state

import io.openfuture.api.domain.PageResponse
import io.openfuture.api.domain.state.AccountDto
import io.openfuture.api.domain.state.StateTransactionDto
import io.openfuture.api.domain.state.WalletDto
import org.springframework.data.domain.PageRequest

interface StateApi {

    // accounts

    fun createAccount(webHook: String, address: String, blockchainId: Int): AccountDto

    fun getAccount(id: Long): AccountDto

    fun updateWebhook(accountId: Long, webHook: String): AccountDto

    fun deleteAccount(id: Long): AccountDto

    fun addWallet(accountId: Long, address: String, blockchainId: Int): AccountDto

    fun deleteWallet(accountId: Long, walletId: Long): AccountDto


    // wallets

    fun getAllWalletsByAccount(accountId: Long): List<WalletDto>

    fun getWalletByAccount(id: Long, accountId: Long): WalletDto


    // transactions

    fun getTransaction(id: Long, walletId: Long): StateTransactionDto

    fun getAllTransactionsByWalletId(walletId: Long, pageRequest: PageRequest): PageResponse<StateTransactionDto>

}
