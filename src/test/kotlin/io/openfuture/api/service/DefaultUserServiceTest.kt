package io.openfuture.api.service

import io.openfuture.api.entity.auth.OpenKey
import io.openfuture.api.entity.auth.User
import io.openfuture.api.repository.UserRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.BDDMockito.given
import org.mockito.Mock

/**
 * @author Alexey Skadorva
 */
internal class DefaultUserServiceTest : ServiceTest() {

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
        val expectedUser = getUser(GOOGLE_ID).apply { openKeys.add(openKey) }

        given(repository.save(user)).willReturn(user.apply { id = ID })
        given(openKeyService.generate(user)).willReturn(openKey)

        val actualUser = service.save(user)

        assertThat(actualUser).isEqualTo(expectedUser)
    }

    private fun getUser(googleId: String): User = User(googleId).apply { id = ID }

}