package io.openfuture.api.config.handler

import io.openfuture.api.entity.auth.User
import io.openfuture.api.service.UserService
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.core.oidc.user.OidcUser
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class AuthenticationSuccessHandler(
        private val userService: UserService
) : SavedRequestAwareAuthenticationSuccessHandler() {

    override fun onAuthenticationSuccess(request: HttpServletRequest, response: HttpServletResponse,
                                         authentication: Authentication) {
        val principal = authentication.principal as OidcUser
        userService.findByGoogleId(principal.subject) ?: userService.save(User(principal.subject))
        response.sendRedirect("/ethereum-scaffolds")
    }

}
