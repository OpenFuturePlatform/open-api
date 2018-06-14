package io.openfuture.api.service

import io.openfuture.api.domain.scaffold.*
import io.openfuture.api.entity.auth.OpenKey
import io.openfuture.api.entity.auth.User
import io.openfuture.api.entity.scaffold.Scaffold
import io.openfuture.api.entity.scaffold.ScaffoldTemplate
import io.openfuture.api.entity.scaffold.Transaction
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

/**
 * @author Kadach Alexey
 */
interface ScaffoldService {

    fun getAll(user: User, pageRequest: Pageable): Page<Scaffold>

    fun get(address: String, user: User): Scaffold

    fun compile(request: CompileScaffoldRequest): CompiledScaffoldDto

    fun deploy(request: DeployScaffoldRequest): Scaffold

    fun save(request: SaveScaffoldRequest): Scaffold

    fun setWebHook(address: String, request: SetWebHookRequest, user: User): Scaffold

    fun getScaffoldSummary(address: String, user: User): ScaffoldSummaryDto

    fun deactivate(address: String, user: User): ScaffoldSummaryDto

    fun getQuota(user: User): ScaffoldQuotaDto

}

interface ScaffoldTemplateService {

    fun getAll(user: User): List<ScaffoldTemplate>

    fun get(id: Long, user: User): ScaffoldTemplate

    fun save(request: SaveScaffoldTemplateRequest, user: User): ScaffoldTemplate

    fun delete(id: Long, user: User): ScaffoldTemplate

}

interface OpenKeyService {

    fun getAll(user: User): List<OpenKey>

    fun get(key: String): OpenKey

    fun find(key: String): OpenKey?

    fun generate(request: GenerateOpenKeyRequest, user: User): OpenKey

    fun generate(user: User): OpenKey

    fun disable(key: String): OpenKey

}

interface UserService {

    fun findByGoogleId(googleId: String): User?

    fun save(user: User): User

}

interface TransactionService {

    fun getAll(scaffold: Scaffold, pageRequest: Pageable): Page<Transaction>

    fun save(transaction: Transaction): Transaction

}