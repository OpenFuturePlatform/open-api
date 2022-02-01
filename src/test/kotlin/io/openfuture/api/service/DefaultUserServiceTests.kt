package io.openfuture.api.service

import io.openfuture.api.config.UnitTest
import io.openfuture.api.config.any
import io.openfuture.api.entity.auth.User
import io.openfuture.api.repository.UserRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.BDDMockito.given
import org.mockito.Mock

internal class DefaultUserServiceTests : UnitTest() {

    @Mock private lateinit var repository: UserRepository

    private lateinit var service: UserService


    @Before
    fun setUp() {
        service = DefaultUserService(repository)
    }

    @Test
    fun findByGoogleIdTest() {
        val googleId = "104113085667282103363"
        val expectedUser = createUser(googleId).apply { id = 1L }

        given(repository.findByGoogleId(googleId)).willReturn(expectedUser)

        val actualUser = service.findByGoogleId(googleId)

        assertThat(actualUser).isEqualTo(expectedUser)
    }

    @Test
    fun saveTest() {
        val user = createUser("104113085667282103363")

        given(repository.save(any(User::class.java))).will { invocation -> invocation.arguments[0] }

        val actualUser = service.save(user)

        assertThat(actualUser.googleId).isEqualTo(user.googleId)
    }

    private fun createUser(googleId: String): User = User(googleId, 1)

}