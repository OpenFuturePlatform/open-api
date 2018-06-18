package io.openfuture.api.service

import io.openfuture.api.config.GOOGLE_ID
import io.openfuture.api.config.OPEN_KEY_VALUE
import io.openfuture.api.config.UnitTest
import io.openfuture.api.config.any
import io.openfuture.api.entity.auth.OpenKey
import io.openfuture.api.entity.auth.User
import io.openfuture.api.exception.NotFoundException
import io.openfuture.api.repository.OpenKeyRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.BDDMockito.given
import org.mockito.Mock
import java.util.*

class DefaultOpenKeyServiceTests : UnitTest() {

    @Mock private lateinit var repository: OpenKeyRepository

    private lateinit var service: OpenKeyService


    @Before
    fun setUp() {
        service = DefaultOpenKeyService(repository)
    }

    @Test
    fun getAllByUser() {
        val user = getUser()
        val expectedOpenKeys = listOf(OpenKey(user), OpenKey(user))

        given(repository.findAllByUser(user)).willReturn(expectedOpenKeys)

        val actualOpenKeys = service.getAll(user)

        assertThat(actualOpenKeys).isEqualTo(expectedOpenKeys)
    }

    @Test
    fun get() {
        val expectedOpenKey = getOpenKey()

        given(repository.findByValue(OPEN_KEY_VALUE)).willReturn(expectedOpenKey)

        val actualOpenKey = service.get(OPEN_KEY_VALUE)

        assertThat(actualOpenKey).isEqualTo(expectedOpenKey)
    }

    @Test(expected = NotFoundException::class)
    fun getWithNotFoundException() {
        given(repository.findByValue(OPEN_KEY_VALUE)).willReturn(null)

        service.get(OPEN_KEY_VALUE)
    }

    @Test
    fun find() {
        val expectedOpenKey = getOpenKey()

        given(repository.findByValue(OPEN_KEY_VALUE)).willReturn(expectedOpenKey)

        val actualOpenKey = service.find(OPEN_KEY_VALUE)

        assertThat(actualOpenKey).isEqualTo(expectedOpenKey)
    }

    @Test
    fun generate() {
        val user = getUser()

        given(repository.save(any(OpenKey::class.java))).will { invocation -> invocation.arguments[0] }

        val actualOpenKey = service.generate(user)

        assertThat(actualOpenKey.user).isEqualTo(user)
        assertThat(actualOpenKey.value).isNotNull()
    }

    private fun getOpenKey(): OpenKey = OpenKey(getUser(), OPEN_KEY_VALUE, true, Date())

    private fun getUser(): User = User(GOOGLE_ID, 0, Collections.emptySet(), Collections.emptySet())

}