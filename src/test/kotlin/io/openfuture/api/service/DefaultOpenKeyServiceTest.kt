package io.openfuture.api.service

import io.openfuture.api.entity.auth.OpenKey
import io.openfuture.api.entity.auth.User
import io.openfuture.api.exception.NotFoundException
import io.openfuture.api.repository.OpenKeyRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.refEq
import org.mockito.BDDMockito.given
import org.mockito.Mock
import java.util.*

/**
 * @author Alexey Skadorva
 */
class DefaultOpenKeyServiceTest : ServiceTest() {

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
        val expectedOpenKey = OpenKey(user)

        given(repository.save(refEq(expectedOpenKey, "value", "expiredDate"))).willReturn(expectedOpenKey)

        val actualOpenKey = service.generate(user)

        assertThat(actualOpenKey).isEqualTo(expectedOpenKey)
    }

    private fun getOpenKey(): OpenKey = OpenKey(getUser(), OPEN_KEY_VALUE, true, Date())

    private fun getUser(): User = User(GOOGLE_ID, 0, Collections.emptySet(), Collections.emptySet())

}