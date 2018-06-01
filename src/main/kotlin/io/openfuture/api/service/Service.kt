package io.openfuture.api.service

import io.openfuture.api.domain.scaffold.DeployScaffoldRequest
import io.openfuture.api.domain.scaffold.ScaffoldSummaryDto
import io.openfuture.api.domain.scaffold.SetWebHookRequest
import io.openfuture.api.entity.auth.OpenKey
import io.openfuture.api.entity.auth.User
import io.openfuture.api.entity.scaffold.Scaffold
import io.openfuture.api.entity.scaffold.Transaction
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

/**
 * @author Kadach Alexey
 */
interface ScaffoldService {

    fun getAll(user: User, pageRequest: Pageable): Page<Scaffold>

    fun get(address: String): Scaffold

    fun deploy(request: DeployScaffoldRequest): Scaffold

    fun setWebHook(address: String, request: SetWebHookRequest): Scaffold

    fun getScaffoldSummary(address: String): ScaffoldSummaryDto

}

interface OpenKeyService {

    fun getAllByUser(user: User): List<OpenKey>

    fun get(key: String): OpenKey

    fun generate(user: User): OpenKey

}

interface UserService {

    fun findByGoogleId(googleId: String): User?

    fun save(user: User): User

}

interface TransactionService {

    fun save(transaction: Transaction): Transaction

}