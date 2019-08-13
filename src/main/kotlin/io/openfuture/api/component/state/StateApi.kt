package io.openfuture.api.component.state

import io.openfuture.api.domain.state.AccountDto
import io.openfuture.api.domain.state.WalletDto

interface StateApi {

    // accounts

    fun createAccount(webHook: String, blockchainId: Int, address: String): Long

    fun getAccount(id: Long): AccountDto

    fun updateWebhook(accountId: Long, webHook: String): AccountDto

    fun deleteAccount(id: Long): AccountDto

    fun addWallet(accountId: Long, blockchainId: Int, address: String): AccountDto

    fun deleteWallet(accountId: Long, walletId: Long): AccountDto


    // wallets

    fun getAllByAccount(accountId: Long): List<WalletDto>

    fun getWallet(id: Long, accountId: Long): WalletDto

}