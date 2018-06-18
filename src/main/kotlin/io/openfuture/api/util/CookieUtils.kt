package io.openfuture.api.util

import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletResponse

object CookieUtils {

    fun add(response: HttpServletResponse, name: String, value: String) {
        val cookie = create(name, value)

        response.addCookie(cookie)
    }

    fun delete(response: HttpServletResponse, name: String, value: String) {
        val cookie = create(name, value)
        cookie.maxAge = 0

        response.addCookie(cookie)
    }

    private fun create(name: String, value: String): Cookie {
        val cookie = Cookie(name, value)
        cookie.path = "/"

        return cookie
    }

}