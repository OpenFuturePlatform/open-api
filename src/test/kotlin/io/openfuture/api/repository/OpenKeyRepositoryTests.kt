package io.openfuture.api.repository

import io.openfuture.api.config.RepositoryTests
import io.openfuture.api.entity.auth.OpenKey
import io.openfuture.api.entity.auth.User
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import java.util.*

internal class OpenKeyRepositoryTests : RepositoryTests() {

    @Autowired
    private lateinit var repository: OpenKeyRepository


    @Test
    fun findByValueAndEnabledIsTrueAndExpiredDateIsNullOrExpiredDateAfter() {
        val user = persistUser()
        val openKeyValue = "op_pk_9de7cbb4-857c-49e9-87d2-fc91428c4c12"
        val expectedOpenKey = OpenKey(user, null, openKeyValue)
        entityManager.persist(expectedOpenKey)

        val actualOpenKey = repository.findByValueAndEnabledIsTrueAndExpiredDateIsNullOrExpiredDateAfter(openKeyValue, Date())

        assertThat(actualOpenKey).isEqualTo(expectedOpenKey)
    }

    @Test
    fun findAllByUser() {
        val user = persistUser()
        val expectedOpenKeys = listOf(OpenKey(user), OpenKey(user))
        entityManager.persist(expectedOpenKeys[0])
        entityManager.persist(expectedOpenKeys[1])

        val actualOpenKeys = repository.findAllByUser(user)

        assertThat(actualOpenKeys.containsAll(expectedOpenKeys)).isTrue()
    }

    private fun persistUser(): User = entityManager.persist(User("googleId"))

}