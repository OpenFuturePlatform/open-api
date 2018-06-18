package io.openfuture.api.service

import io.openfuture.api.config.UnitTest
import io.openfuture.api.config.any
import io.openfuture.api.entity.auth.OpenKey
import io.openfuture.api.entity.auth.User
import io.openfuture.api.repository.UserRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.BDDMockito.given
import org.mockito.Mock

internal class DefaultUserServiceTests : UnitTest() {

    @Mock private lateinit var repository: UserRepository
    @Mock private lateinit var openKeyService: OpenKeyService

    private lateinit var service: UserService


    @Before
    fun setUp() {
        service = DefaultUserService(repository, openKeyService)
    }

    @Test
    fun findByGoogleId() {
        val googleId = "104113085667282103363"
        val expectedUser = getUser(googleId)

        given(repository.findByGoogleId(googleId)).willReturn(expectedUser)

        val actualUser = service.findByGoogleId(googleId)

        assertThat(actualUser).isEqualTo(expectedUser)
    }

    @Test
    fun save() {
        val user = User("104113085667282103363")
        val openKey = OpenKey(user, "op_pk_9de7cbb4-857c-49e9-87d2-fc91428c4c12")

        given(repository.save(any(User::class.java))).will { invocation -> invocation.arguments[0] }
        given(openKeyService.generate(user)).willReturn(openKey)

        val actualUser = service.save(user)

        assertThat(actualUser.openKeys.first()).isEqualTo(openKey)
        assertThat(actualUser.googleId).isEqualTo(user.googleId)
    }

    private fun getUser(googleId: String): User = User(googleId).apply { id = 1L }

}