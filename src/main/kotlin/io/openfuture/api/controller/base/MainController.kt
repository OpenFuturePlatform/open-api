package io.openfuture.api.controller.base

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

/**
 * @author Kadach Alexey
 */
@Controller
class MainController {

    @GetMapping("/logout")
    fun logout(): String {
        SecurityContextHolder.getContext().authentication = null
        return "redirect:/"
    }

    @GetMapping("/", "/scaffold", "/scaffolds")
    fun frontend() = "frontend"

}