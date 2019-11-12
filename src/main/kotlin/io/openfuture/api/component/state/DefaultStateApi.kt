package io.openfuture.api.component.state

import io.openfuture.api.client.BodyConverter
import io.openfuture.api.client.HttpClientWrapper
import io.openfuture.api.domain.PageResponse
import io.openfuture.api.domain.state.*
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Component

@Component
class DefaultStateApi(
        private val clientHttp: HttpClientWrapper,
        @Value("\${open.state.url}") private val stateUrl: String
) : StateApi {

    override fun saveOpenScaffold(webHook: String, address: String) :OpenScaffoldDto {
        val url = "$stateUrl/open-scaffolds"
        val request = SaveScaffoldRequest(address, webHook)
        val response = clientHttp.post(url, prepareHeader(), request)

        return BodyConverter.deserialize(response.entity)
    }

    override fun createAccount(webHook: String?, address: String, blockchainId: Int): AccountDto {
        val url = "$stateUrl/accounts"
        val request = CreateAccountRequest(webHook, setOf(CreateIntegrationRequest(address, blockchainId)))
        val response = clientHttp.post(url, prepareHeader(), request)

        return BodyConverter.deserialize(response.entity)
    }

    override fun getAccount(id: Long): AccountDto {
        val url = "$stateUrl/accounts/$id"
        val response = clientHttp.get(url, prepareHeader())

        return BodyConverter.deserialize(response.entity)
    }

    override fun updateWebhook(accountId: Long, webHook: String): AccountDto {
        val url = "$stateUrl/accounts"
        val request = UpdateAccountWebHookRequest(accountId, webHook)
        val response = clientHttp.put(url, prepareHeader(), request)

        return BodyConverter.deserialize(response.entity)
    }

    override fun deleteAccount(id: Long): AccountDto {
        val url = "$stateUrl/accounts/$id"
        val response = clientHttp.delete(url, prepareHeader())

        return BodyConverter.deserialize(response.entity)
    }

    override fun addWallet(accountId: Long, address: String, blockchainId: Int): AccountDto {
        val url = "$stateUrl/accounts/$accountId/wallets"
        val request = AddWalletRequest(setOf(CreateIntegrationRequest(address, blockchainId)))
        val response = clientHttp.post(url, prepareHeader(), request)

        return BodyConverter.deserialize(response.entity)
    }

    override fun deleteWallet(accountId: Long, address: String, blockchainId: Int): AccountDto {
        val url = "$stateUrl/accounts/$accountId/wallets/delete"
        val request = DeleteWalletRequest(accountId, address, blockchainId)
        val response = clientHttp.post(url, prepareHeader(), request)

        return BodyConverter.deserialize(response.entity)
    }

    override fun getAllWalletsByAccount(accountId: Long): List<WalletDto> {
        val url = "$stateUrl/accounts/$accountId/wallets"
        val response = clientHttp.get(url, prepareHeader())

        return BodyConverter.deserialize(response.entity)
    }

    override fun getWalletByAccount(id: Long, accountId: Long): WalletDto {
        val url = "$stateUrl/accounts/$accountId/wallets/$id"
        val response = clientHttp.get(url, prepareHeader())

        return BodyConverter.deserialize(response.entity)
    }

    override fun getTransaction(id: Long, walletId: Long): StateTransactionDto {
        val url = "$stateUrl/wallets/$walletId/transactions/$id"
        val response = clientHttp.get(url, prepareHeader())

        return BodyConverter.deserialize(response.entity)
    }

    override fun getAllTransactionsByWalletId(walletId: Long, pageRequest: PageRequest): PageResponse<StateTransactionDto> {
        val url = "$stateUrl/wallets/$walletId/transactions"
        val response = clientHttp.getPageable(url, prepareHeader(), pageRequest)

        return BodyConverter.deserialize(response.entity)
    }

    private fun prepareHeader(): Map<String, String> {
        return mapOf("Content-Type" to "application/json")
    }

}
