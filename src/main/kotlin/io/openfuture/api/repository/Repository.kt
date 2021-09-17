package io.openfuture.api.repository

import io.openfuture.api.entity.application.Application
import io.openfuture.api.entity.auth.OpenKey
import io.openfuture.api.entity.auth.User
import io.openfuture.api.entity.scaffold.*
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.NoRepositoryBean
import org.springframework.stereotype.Repository

@NoRepositoryBean
interface BaseRepository<T> : JpaRepository<T, Long>

@Repository
interface UserRepository : BaseRepository<User> {

    fun findByGoogleId(googleId: String): User?

}

@Repository
interface EthereumScaffoldRepository : BaseRepository<EthereumScaffold> {

    fun findByAddressAndOpenKeyUser(address: String, user: User): EthereumScaffold?

    fun findByAddress(address: String): EthereumScaffold?

    fun findByAddressIgnoreCase(address: String): EthereumScaffold?

    fun findAllByOpenKeyUserOrderByIdDesc(user: User, pageable: Pageable): Page<EthereumScaffold>

}

@Repository
interface OpenScaffoldRepository : BaseRepository<OpenScaffold> {

    fun findAllByOpenKeyUserOrderByIdDesc(user: User, pageable: Pageable): Page<OpenScaffold>
}

@Repository
interface EthereumScaffoldPropertyRepository : BaseRepository<EthereumScaffoldProperty> {

    fun findAllByEthereumScaffoldAddress(address: String): List<EthereumScaffoldProperty>

}

@Repository
interface OpenKeyRepository : BaseRepository<OpenKey> {

    fun findByValueAndEnabledIsTrue(value: String): OpenKey?

    fun findAllByUser(user: User): List<OpenKey>

}

@Repository
interface EthereumTransactionRepository : BaseRepository<EthereumTransaction> {

    fun findAllByEthereumScaffoldOrderByDateDesc(ethereumScaffold: EthereumScaffold, pageable: Pageable): Page<EthereumTransaction>

    fun findByHashAndIndex(hash: String, index: String): EthereumTransaction?

}

@Repository
interface EthereumScaffoldTemplateRepository : BaseRepository<EthereumScaffoldTemplate> {

    fun findAllByDeletedIsFalse(): List<EthereumScaffoldTemplate>

}

@Repository
interface ApplicationRepository : BaseRepository<Application> {

    fun findAllByUser(user: User, pageable: Pageable): Page<Application>

}

@Repository
interface ScaffoldTemplatePropertyRepository : BaseRepository<EthereumScaffoldTemplateProperty>

@Repository
interface EthereumScaffoldSummaryRepository : BaseRepository<EthereumScaffoldSummary> {

    fun findByEthereumScaffold(ethereumScaffold: EthereumScaffold): EthereumScaffoldSummary?

    fun countByEnabledIsFalseAndEthereumScaffoldOpenKeyUser(user: User): Int

}

@Repository
interface ShareHolderRepository : BaseRepository<EthereumShareHolder> {

    fun deleteAllBySummary(summary: EthereumScaffoldSummary)

}