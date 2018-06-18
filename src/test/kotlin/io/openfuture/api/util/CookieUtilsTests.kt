package io.openfuture.api.util

import org.apache.commons.lang3.StringUtils
import org.assertj.core.api.Java6Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.Mock
import org.mockito.Mockito.doNothing
import org.mockito.junit.MockitoJUnitRunner
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletResponse

@RunWith(MockitoJUnitRunner::class)
class CookieUtilsTests {

    @Mock private lateinit var response: HttpServletResponse


    @Test
    fun add() {
        val name = "name"
        val value = "value"

        val captor = ArgumentCaptor.forClass(Cookie::class.java)
        doNothing().`when`(response).addCookie(captor.capture())

        CookieUtils.add(response, name, value)

        assertThat(captor.value.name).isEqualTo(name)
        assertThat(captor.value.value).isEqualTo(value)
        assertThat(captor.value.path).isEqualTo("/")
    }

    @Test
    fun delete() {
        val name = "name"
        val value = StringUtils.EMPTY

        val captor = ArgumentCaptor.forClass(Cookie::class.java)
        doNothing().`when`(response).addCookie(captor.capture())

        CookieUtils.delete(response, name, value)

        assertThat(captor.value.name).isEqualTo(name)
        assertThat(captor.value.value).isEqualTo(value)
        assertThat(captor.value.maxAge).isEqualTo(0)
    }

}
