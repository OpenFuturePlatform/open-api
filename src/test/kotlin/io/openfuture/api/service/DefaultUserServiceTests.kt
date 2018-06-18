package io.openfuture.api.service

import io.openfuture.api.config.*
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
        val expectedUser = getUser(GOOGLE_ID)

        given(repository.findByGoogleId(GOOGLE_ID)).willReturn(expectedUser)

        val actualUser = service.findByGoogleId(GOOGLE_ID)

        assertThat(actualUser).isEqualTo(expectedUser)
    }

    @Test
    fun save() {
        val user = User(GOOGLE_ID)
        val openKey = OpenKey(user, OPEN_KEY_VALUE)

        given(repository.save(any(User::class.java))).will { invocation -> invocation.arguments[0] }
        given(openKeyService.generate(user)).willReturn(openKey)

        val actualUser = service.save(user)

        assertThat(actualUser.openKeys.first()).isEqualTo(openKey)
        assertThat(actualUser.googleId).isEqualTo(user.googleId)
    }

    private fun getUser(googleId: String): User = User(googleId).apply { id = ID }

}