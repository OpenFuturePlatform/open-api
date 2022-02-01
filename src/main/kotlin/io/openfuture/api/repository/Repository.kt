package io.openfuture.api.repository

import io.openfuture.api.entity.application.Application
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
interface EthereumScaffoldRepository : BaseRepository<EthereumScaffold> {

    fun findByAddress(address: String): EthereumScaffold?

    fun findByAddressIgnoreCase(address: String): EthereumScaffold?

    override fun findAll(pageable: Pageable): Page<EthereumScaffold>

}

@Repository
interface OpenScaffoldRepository : BaseRepository<OpenScaffold> {

    override fun findAll(pageable: Pageable): Page<OpenScaffold>
}

@Repository
interface EthereumScaffoldPropertyRepository : BaseRepository<EthereumScaffoldProperty> {

    fun findAllByEthereumScaffoldAddress(address: String): List<EthereumScaffoldProperty>

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

    fun findFirstByApiAccessKey(apiAccessKey : String): Optional<Application>

    fun findFirstByApiAccessKeyAndApiSecretKey(apiAccessKey : String, apiSecretKey : String): Optional<Application>

}

@Repository
interface ScaffoldTemplatePropertyRepository : BaseRepository<EthereumScaffoldTemplateProperty>

@Repository
interface EthereumScaffoldSummaryRepository : BaseRepository<EthereumScaffoldSummary> {

    fun findByEthereumScaffold(ethereumScaffold: EthereumScaffold): EthereumScaffoldSummary?

    fun countByEnabledIsFalse(): Int

}

@Repository
interface ShareHolderRepository : BaseRepository<EthereumShareHolder> {

    fun deleteAllBySummary(summary: EthereumScaffoldSummary)

}