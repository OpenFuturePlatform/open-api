package io.openfuture.api.controller.base

import io.openfuture.api.config.propety.AuthorizationProperties
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletResponse

@Controller
class MainController(
        private val properties: AuthorizationProperties
) {

    @GetMapping("/logout")
    fun logout(response: HttpServletResponse): String {
        val cookie = Cookie(properties.cookieName, null)
        response.addCookie(cookie)
        return "redirect:/"
    }

    @GetMapping("/", "/scaffold", "/scaffolds", "/scaffolds/**")
    fun frontend() = "frontend"

}