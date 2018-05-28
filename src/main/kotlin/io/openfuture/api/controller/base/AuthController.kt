package io.openfuture.api.controller.base

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

/**
 * @author Kadach Alexey
 */
@Controller
class AuthController {

    @GetMapping("/logout")
    fun get(): String {
        SecurityContextHolder.getContext().authentication = null
        return "redirect:/"
    }

}