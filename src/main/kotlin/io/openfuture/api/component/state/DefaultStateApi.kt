package io.openfuture.api.component.state

import io.openfuture.api.client.BodyConverter
import io.openfuture.api.client.HttpClientWrapper
import io.openfuture.api.domain.state.*
import io.openfuture.api.exception.StateApiException
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Component

@Component
class DefaultStateApi(
        private val clientHttp: HttpClientWrapper,
        @Value("\${open.state.url}") private val stateUrl: String
) : StateApi {

    override fun createAccount(webHook: String, address: String, blockchainId: Int): AccountDto {
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

        return BodyConverter.deserialize(response.entity)    }

    override fun deleteAccount(id: Long): AccountDto {
        val url = "$stateUrl/accounts/$id"
        val response = clientHttp.delete(url, prepareHeader())

        return BodyConverter.deserialize(response.entity)
    }

    override fun addWallet(accountId: Long, blockchainId: Int, address: String): AccountDto {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteWallet(accountId: Long, walletId: Long): AccountDto {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getAllByAccount(accountId: Long): List<WalletDto> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getWallet(id: Long, accountId: Long): WalletDto {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getTransaction(id: Long, walletId: Long): StateTransactionDto {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getAllByWalletId(walletId: Long, pageable: Pageable): Page<StateTransactionDto> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun prepareHeader(): Map<String, String> {
        return mapOf("Content-Type" to "application/json")
    }

}
