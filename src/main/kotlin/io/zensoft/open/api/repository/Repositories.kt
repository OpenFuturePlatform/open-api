package io.zensoft.open.api.repository

import io.zensoft.open.api.model.Scaffold
import io.zensoft.open.api.model.User
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
interface ScaffoldRepository: BaseRepository<Scaffold>