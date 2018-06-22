package io.openfuture.api.service

import io.openfuture.api.config.UnitTest
import io.openfuture.api.config.any
import io.openfuture.api.config.eq
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
        val user = createUser()
        val expectedOpenKeys = listOf(OpenKey(user), OpenKey(user))

        given(repository.findAllByUser(user)).willReturn(expectedOpenKeys)

        val actualOpenKeys = service.getAll(user)

        assertThat(actualOpenKeys).isEqualTo(expectedOpenKeys)
    }

    @Test
    fun get() {
        val openKeyValue = "op_pk_9de7cbb4-857c-49e9-87d2-fc91428c4c12"
        val expectedOpenKey = createOpenKey()

        given(repository.findByValueAndEnabledIsTrueAndExpiredDateIsNullOrExpiredDateAfter(eq(openKeyValue),
                any(Date::class.java))).willReturn(expectedOpenKey)

        val actualOpenKey = service.get(openKeyValue)

        assertThat(actualOpenKey).isEqualTo(expectedOpenKey)
    }

    @Test(expected = NotFoundException::class)
    fun getWithNotFoundException() {
        val openKeyValue = "op_pk_9de7cbb4-857c-49e9-87d2-fc91428c4c12"

        given(repository.findByValueAndEnabledIsTrueAndExpiredDateIsNullOrExpiredDateAfter(eq(openKeyValue),
                any(Date::class.java))).willReturn(null)

        service.get(openKeyValue)
    }

    @Test
    fun find() {
        val openKeyValue = "op_pk_9de7cbb4-857c-49e9-87d2-fc91428c4c12"
        val expectedOpenKey = createOpenKey()

        given(repository.findByValueAndEnabledIsTrueAndExpiredDateIsNullOrExpiredDateAfter(eq(openKeyValue),
                any(Date::class.java))).willReturn(expectedOpenKey)

        val actualOpenKey = service.find(openKeyValue)

        assertThat(actualOpenKey).isEqualTo(expectedOpenKey)
    }

    @Test
    fun generate() {
        val user = createUser()

        given(repository.save(any(OpenKey::class.java))).will { invocation -> invocation.arguments[0] }

        val actualOpenKey = service.generate(user)

        assertThat(actualOpenKey.user).isEqualTo(user)
        assertThat(actualOpenKey.value).isNotNull()
    }

    @Test
    fun disable() {
        val openKeyValue = "op_pk_9de7cbb4-857c-49e9-87d2-fc91428c4c12"
        val expectedOpenKey = createOpenKey()

        given(repository.findByValueAndEnabledIsTrueAndExpiredDateIsNullOrExpiredDateAfter(eq(openKeyValue),
                any(Date::class.java))).willReturn(expectedOpenKey)

        given(repository.save(any(OpenKey::class.java))).will { invocation -> invocation.arguments[0] }

        val actualOpenKey = service.disable(openKeyValue)

        assertThat(actualOpenKey.value).isEqualTo(openKeyValue)
        assertThat(actualOpenKey.enabled).isFalse()
    }

    private fun createOpenKey(): OpenKey = OpenKey(createUser(), null, "op_pk_9de7cbb4-857c-49e9-87d2-fc91428c4c12", true)

    private fun createUser(): User = User("104113085667282103363", 0, Collections.emptySet(), Collections.emptySet())

}