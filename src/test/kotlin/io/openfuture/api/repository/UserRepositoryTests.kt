package io.openfuture.api.repository

import io.openfuture.api.config.RepositoryTests
import io.openfuture.api.entity.auth.User
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

internal class UserRepositoryTests : RepositoryTests() {

    @Autowired private lateinit var repository: UserRepository


    @Test
    fun findByGoogleId() {
        val googleId = "googleId"
        val expectedUser = User(googleId)
        entityManager.persist(expectedUser)

        val actualUser = repository.findByGoogleId(googleId)

        assertThat(actualUser).isEqualTo(expectedUser)
    }

}