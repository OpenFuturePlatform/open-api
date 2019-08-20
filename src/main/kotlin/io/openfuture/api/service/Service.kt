package io.openfuture.api.service

import io.openfuture.api.domain.holder.AddShareHolderRequest
import io.openfuture.api.domain.holder.UpdateShareHolderRequest
import io.openfuture.api.domain.scaffold.*
import io.openfuture.api.entity.auth.OpenKey
import io.openfuture.api.entity.auth.User
import io.openfuture.api.entity.scaffold.Scaffold
import io.openfuture.api.entity.scaffold.ScaffoldSummary
import io.openfuture.api.entity.scaffold.ScaffoldTemplate
import io.openfuture.api.entity.scaffold.Transaction
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface ScaffoldService {

    fun getAll(user: User, pageRequest: Pageable): Page<Scaffold>

    fun get(address: String, user: User): Scaffold

    fun get(address: String): Scaffold

    fun compile(request: CompileScaffoldRequest): CompiledScaffoldDto

    fun deploy(request: DeployScaffoldRequest): Scaffold

    fun save(request: SaveScaffoldRequest): Scaffold

    fun update(address: String, user: User, request: UpdateScaffoldRequest): Scaffold

    fun setWebHook(address: String, request: SetWebHookRequest, user: User): Scaffold

    fun getQuota(user: User): ScaffoldQuotaDto

    fun getScaffoldSummary(address: String, user: User, force: Boolean = false): ScaffoldSummary

    fun deactivate(address: String, user: User): ScaffoldSummary

    fun activate(address: String, user: User): ScaffoldSummary

    fun addShareHolder(address: String, user: User, request: AddShareHolderRequest): ScaffoldSummary

    fun updateShareHolder(address: String, user: User,
                          holderAddress: String, request: UpdateShareHolderRequest): ScaffoldSummary

    fun removeShareHolder(address: String, user: User, holderAddress: String): ScaffoldSummary

}

interface ScaffoldTemplateService {

    fun getAll(): List<ScaffoldTemplate>

    fun get(id: Long): ScaffoldTemplate

    fun save(request: SaveScaffoldTemplateRequest): ScaffoldTemplate

    fun delete(id: Long): ScaffoldTemplate

}

interface OpenKeyService {

    fun getAll(user: User): List<OpenKey>

    fun get(key: String): OpenKey

    fun find(key: String): OpenKey?

    fun generate(request: GenerateOpenKeyRequest, user: User): OpenKey

    fun generate(user: User): OpenKey

    fun disable(key: String): OpenKey

    fun update(openKey: OpenKey): OpenKey

}

interface UserService {

    fun findByGoogleId(googleId: String): User?

    fun save(user: User): User

}

interface TransactionService {

    fun getAll(scaffold: Scaffold, pageRequest: Pageable): Page<Transaction>

    fun find(hash: String, index: String): Transaction?

    fun save(transaction: Transaction): Transaction

}