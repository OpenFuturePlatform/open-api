package io.openfuture.api.controller.base

import io.openfuture.api.config.propety.AuthorizationProperties
import io.openfuture.api.config.propety.WidgetProperties
import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletResponse

@Controller
class MainController(
        private val properties: AuthorizationProperties,
        private val widgetProperties: WidgetProperties
) {

    @GetMapping("/logout")
    fun logout(response: HttpServletResponse): String {
        val cookie = Cookie(properties.cookieName, null)
        response.addCookie(cookie)
        return "redirect:/"
    }

    @GetMapping("/", "/scaffold", "/scaffolds", "/scaffolds/**", "/keys", "/keys/**")
    fun frontend() = "frontend"

    @GetMapping("/widget/{address}")
    fun widget(@PathVariable address: String, model: ModelMap): String {
        model["address"] = address
        model["host"] = widgetProperties.host
        return "widget"
    }

}