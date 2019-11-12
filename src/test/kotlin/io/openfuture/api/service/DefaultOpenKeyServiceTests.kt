package io.openfuture.api.service

import io.openfuture.api.config.UnitTest
import io.openfuture.api.config.any
import io.openfuture.api.config.eq
import io.openfuture.api.entity.auth.OpenKey
import io.openfuture.api.entity.auth.User
import io.openfuture.api.exception.NotFoundException
import io.openfuture.api.repository.OpenKeyRepository
import org.apache.commons.lang3.time.DateUtils
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
    fun getAllByUserTest() {
        val user = createUser()
        val expectedOpenKeys = listOf(OpenKey(user), OpenKey(user))

        given(repository.findAllByUser(user)).willReturn(expectedOpenKeys)

        val actualOpenKeys = service.getAll(user)

        assertThat(actualOpenKeys).isEqualTo(expectedOpenKeys)
    }

    @Test
    fun getTest() {
        val openKeyValue = "op_pk_9de7cbb4-857c-49e9-87d2-fc91428c4c12"
        val expectedOpenKey = createOpenKey()

        given(repository.findByValueAndEnabledIsTrue(eq(openKeyValue))).willReturn(expectedOpenKey)

        val actualOpenKey = service.get(openKeyValue)

        assertThat(actualOpenKey).isEqualTo(expectedOpenKey)
    }

    @Test(expected = NotFoundException::class)
    fun getWhenOpenKeyNotFoundShouldThrowExceptionTest() {
        val openKeyValue = "op_pk_9de7cbb4-857c-49e9-87d2-fc91428c4c12"

        given(repository.findByValueAndEnabledIsTrue(eq(openKeyValue))).willReturn(null)

        service.get(openKeyValue)
    }

    @Test
    fun findTest() {
        val openKeyValue = "op_pk_9de7cbb4-857c-49e9-87d2-fc91428c4c12"
        val expectedOpenKey = createOpenKey()

        given(repository.findByValueAndEnabledIsTrue(eq(openKeyValue))).willReturn(expectedOpenKey)

        val actualOpenKey = service.find(openKeyValue)

        assertThat(actualOpenKey).isEqualTo(expectedOpenKey)
    }

    @Test
    fun findWhenDateExpiredShouldReturnNull() {
        val openKeyValue = "op_pk_9de7cbb4-857c-49e9-87d2-fc91428c4c12"
        val expectedOpenKey = createOpenKey(DateUtils.addMinutes(Date(), -1))

        given(repository.findByValueAndEnabledIsTrue(eq(openKeyValue))).willReturn(expectedOpenKey)

        val actualOpenKey = service.find(openKeyValue)

        assertThat(actualOpenKey).isNull()
    }

    @Test
    fun generateTest() {
        val user = createUser()

        given(repository.save(any(OpenKey::class.java))).will { invocation -> invocation.arguments[0] }

        val actualOpenKey = service.generate(user)

        assertThat(actualOpenKey.user).isEqualTo(user)
        assertThat(actualOpenKey.value).isNotNull()
    }

    @Test
    fun disableTest() {
        val openKeyValue = "op_pk_9de7cbb4-857c-49e9-87d2-fc91428c4c12"
        val expectedOpenKey = createOpenKey()

        given(repository.findByValueAndEnabledIsTrue(eq(openKeyValue))).willReturn(expectedOpenKey)

        given(repository.save(any(OpenKey::class.java))).will { invocation -> invocation.arguments[0] }

        val actualOpenKey = service.disable(openKeyValue)

        assertThat(actualOpenKey.value).isEqualTo(openKeyValue)
        assertThat(actualOpenKey.enabled).isFalse()
    }

    private fun createOpenKey(): OpenKey = createOpenKey(null)

    private fun createOpenKey(date: Date?): OpenKey = OpenKey(createUser(), date, "op_pk_9de7cbb4-857c-49e9-87d2-fc91428c4c12", enabled = true)

    private fun createUser(): User = User("104113085667282103363", 0, Collections.emptySet(), Collections.emptySet())

}