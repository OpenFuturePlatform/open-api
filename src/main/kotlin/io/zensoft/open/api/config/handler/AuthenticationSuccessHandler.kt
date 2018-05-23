package io.zensoft.open.api.config.handler

import io.zensoft.open.api.model.User
import io.zensoft.open.api.repository.UserRepository
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.core.oidc.user.OidcUser
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * @author Kadach Alexey
 */
class AuthenticationSuccessHandler(
        private val repository: UserRepository
) : SavedRequestAwareAuthenticationSuccessHandler() {

    override fun onAuthenticationSuccess(request: HttpServletRequest, response: HttpServletResponse,
                                         authentication: Authentication) {
        val principal = authentication.principal as OidcUser
        val persistUser = repository.findById(principal.subject)

        if (!persistUser.isPresent) {
            repository.save(User(principal))
        }

        response.sendRedirect("/scaffolds")
    }

}