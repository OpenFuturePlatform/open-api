package io.openfuture.api.repository

import io.openfuture.api.config.RepositoryTests
import io.openfuture.api.domain.PageRequest
import io.openfuture.api.entity.application.Application
import io.openfuture.api.entity.auth.User
import io.openfuture.api.entity.scaffold.Currency
import org.assertj.core.api.Assertions
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

internal class ApplicationRepositoryTests: RepositoryTests() {
    @Autowired
    private lateinit var repository: ApplicationRepository

    @Test
    fun findAllByUserTest() {
        val user = persistUser()
        val expectedApplication = createApplication(user)
        entityManager.persist(expectedApplication)

        val actualGateways = repository.findAllByUser(user, PageRequest())

        Assertions.assertThat(actualGateways.contains(expectedApplication)).isTrue()
    }

    private fun persistUser(): User = entityManager.persist(User("googleId"))

    private fun createApplication(user: User): Application =
        Application("Gateway 1",user, "https://openfuture.io/webhook",true, "gOd3mUAJiGpCObdmB9/g", "Uoufw+RTaK5+L0XXWJePZjS3HWwv6/ewkJJg8e/d")
}