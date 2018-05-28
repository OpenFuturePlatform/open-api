package io.zensoft.open.api.repository

import io.zensoft.open.api.model.auth.OpenKey
import io.zensoft.open.api.model.auth.User
import io.zensoft.open.api.model.scaffold.Scaffold
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.NoRepositoryBean
import org.springframework.stereotype.Repository

@NoRepositoryBean
interface BaseRepository<T> : JpaRepository<T, Long>

@Repository
interface UserRepository: BaseRepository<User> {

    fun findByGoogleId(googleId: String): User?

}

@Repository
interface ScaffoldRepository: BaseRepository<Scaffold> {

    fun findByAddress(address: String): Scaffold?

    fun findAllByUser(user: User, pageable: Pageable): Page<Scaffold>

}

interface OpenKeyRepository: BaseRepository<OpenKey> {

    fun findByValueAndUser(value: String, user: User): OpenKey?

    fun findAllByUser(user: User): List<OpenKey>

}