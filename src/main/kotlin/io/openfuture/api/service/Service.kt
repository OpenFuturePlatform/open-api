package io.openfuture.api.service

import io.openfuture.api.domain.application.ApplicationAccessKey
import io.openfuture.api.domain.application.ApplicationRequest
import io.openfuture.api.domain.holder.AddEthereumShareHolderRequest
import io.openfuture.api.domain.holder.UpdateEthereumShareHolderRequest
import io.openfuture.api.domain.key.GenerateWalletRequest
import io.openfuture.api.domain.key.KeyWalletDto
import io.openfuture.api.domain.key.WalletApiCreateRequest
import io.openfuture.api.domain.scaffold.*
import io.openfuture.api.domain.state.StateSignRequest
import io.openfuture.api.domain.transaction.TransactionDto
import io.openfuture.api.entity.application.Application
import io.openfuture.api.entity.auth.User
import io.openfuture.api.entity.scaffold.*
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface EthereumScaffoldService {

    fun getAll(user: User, pageRequest: Pageable): Page<EthereumScaffold>

    fun get(address: String, user: User): EthereumScaffold

    fun get(address: String): EthereumScaffold

    fun compile(request: CompileEthereumScaffoldRequest, user: User): CompiledScaffoldDto

    fun deploy(request: DeployEthereumScaffoldRequest, user: User): EthereumScaffold

    fun save(request: SaveEthereumScaffoldRequest, user: User): EthereumScaffold

    fun update(address: String, user: User, request: UpdateEthereumScaffoldRequest): EthereumScaffold

    fun setWebHook(address: String, request: SetWebHookRequest, user: User): EthereumScaffold

    fun getQuota(user: User): EthereumScaffoldQuotaDto

    fun getScaffoldSummary(address: String, user: User, force: Boolean = false): EthereumScaffoldSummary

    fun deactivate(address: String, user: User): EthereumScaffoldSummary

    fun activate(address: String, user: User): EthereumScaffoldSummary

    fun addShareHolder(address: String, user: User, request: AddEthereumShareHolderRequest): EthereumScaffoldSummary

    fun updateShareHolder(address: String, user: User,
                          holderAddress: String, request: UpdateEthereumShareHolderRequest): EthereumScaffoldSummary

    fun removeShareHolder(address: String, user: User, holderAddress: String): EthereumScaffoldSummary

}


interface OpenScaffoldService {

    fun getAll(user: User, pageRequest: Pageable): Page<OpenScaffold>

    fun save(request: SaveOpenScaffoldRequest): OpenScaffold

}

interface EthereumScaffoldTemplateService {

    fun getAll(): List<EthereumScaffoldTemplate>

    fun get(id: Long): EthereumScaffoldTemplate

    fun save(request: SaveEthereumScaffoldTemplateRequest): EthereumScaffoldTemplate

    fun delete(id: Long): EthereumScaffoldTemplate

}

interface UserService {

    fun findByGoogleId(googleId: String): User?

    fun save(user: User): User

}

interface EthereumTransactionService {

    fun getAll(ethereumScaffold: EthereumScaffold, pageRequest: Pageable): Page<EthereumTransaction>

    fun find(hash: String, index: String): EthereumTransaction?

    fun save(transaction: EthereumTransaction): EthereumTransaction

}

interface ApplicationService {

    fun getAll(user: User, pageRequest: Pageable): Page<Application>

    fun getById(id: Long): Application

    fun getByAccessKey(accessKey: String): Application

    fun save(request: ApplicationRequest, user: User, applicationAccessKey: ApplicationAccessKey): Application

    fun delete(id: Long)

}

interface ApplicationWalletService {

    fun generateWallet(request: GenerateWalletRequest, user: User): KeyWalletDto

    fun getAllWallets(id: Long): Array<KeyWalletDto>

    fun deleteWallet(applicationId: String, address: String)

    fun getAddressTransactions(address: String) : Array<TransactionDto>

    fun generateSignature(address: String, request: StateSignRequest): String
}

interface WalletApiService {

    fun generateWallet(walletApiCreateRequest: WalletApiCreateRequest, application: Application, user: User): KeyWalletDto

}