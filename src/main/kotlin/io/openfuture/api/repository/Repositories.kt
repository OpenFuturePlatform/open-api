package io.openfuture.api.repository

import io.openfuture.api.model.auth.OpenKey
import io.openfuture.api.model.auth.User
import io.openfuture.api.model.scaffold.Scaffold
import io.openfuture.api.model.scaffold.Transaction
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
interface ScaffoldRepository : BaseRepository<Scaffold> {

    fun findByAddress(address: String): Scaffold?

    fun findAllByUser(user: User, pageable: Pageable): Page<Scaffold>

}

@Repository
interface OpenKeyRepository : BaseRepository<OpenKey> {

    fun findByValueAndUser(value: String, user: User): OpenKey?

    fun findAllByUser(user: User): List<OpenKey>

}

@Repository
interface TransactionRepository : BaseRepository<Transaction>