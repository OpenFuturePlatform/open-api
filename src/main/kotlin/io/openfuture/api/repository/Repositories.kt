package io.openfuture.api.repository

import io.openfuture.api.entity.auth.OpenKey
import io.openfuture.api.entity.auth.User
import io.openfuture.api.entity.scaffold.*
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.NoRepositoryBean
import org.springframework.stereotype.Repository
import java.util.*

@NoRepositoryBean
interface BaseRepository<T> : JpaRepository<T, Long>

@Repository
interface UserRepository : BaseRepository<User> {

    fun findByGoogleId(googleId: String): User?

}

@Repository
interface ScaffoldRepository : BaseRepository<Scaffold> {

    fun findByAddressAndOpenKeyUser(address: String, user: User): Scaffold?

    fun findByAddress(address: String): Scaffold?

    fun findAllByOpenKeyUser(user: User, pageable: Pageable): Page<Scaffold>

}

@Repository
interface ScaffoldPropertyRepository : BaseRepository<ScaffoldProperty>

@Repository
interface OpenKeyRepository : BaseRepository<OpenKey> {

    fun findByValueAndEnabledIsTrueAndExpiredDateIsNullOrExpiredDateAfter(value: String, date: Date): OpenKey?

    fun findAllByUser(user: User): List<OpenKey>

}

@Repository
interface TransactionRepository : BaseRepository<Transaction> {

    fun findAllByScaffold(scaffold: Scaffold, pageable: Pageable): Page<Transaction>

}

@Repository
interface ScaffoldTemplateRepository : BaseRepository<ScaffoldTemplate> {

    fun findAllByDeletedIsFalse(): List<ScaffoldTemplate>

}

@Repository
interface ScaffoldTemplatePropertyRepository : BaseRepository<ScaffoldTemplateProperty>

@Repository
interface ScaffoldSummaryRepository : BaseRepository<ScaffoldSummary> {

    fun findByScaffold(scaffold: Scaffold): ScaffoldSummary?

    fun countByEnabledIsFalseAndScaffoldOpenKeyUser(user: User): Int

}

@Repository
interface ShareHolderRepository : BaseRepository<ShareHolder> {

    fun deleteAllBySummary(summary: ScaffoldSummary)

}