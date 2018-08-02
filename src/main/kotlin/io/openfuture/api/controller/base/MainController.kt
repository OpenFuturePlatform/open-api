package io.openfuture.api.controller.base

import io.openfuture.api.config.propety.AuthorizationProperties
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.servlet.ModelAndView
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

    @GetMapping("/", "/scaffold", "/scaffolds", "/scaffolds/**", "/keys", "/keys/**")
    fun frontend() = "frontend"

    @GetMapping("/widget/{address}")
    fun widget(@PathVariable address: String, modelAndView: ModelAndView): String {
        modelAndView.modelMap.addAttribute("address", address)
        return "widget"
    }

}