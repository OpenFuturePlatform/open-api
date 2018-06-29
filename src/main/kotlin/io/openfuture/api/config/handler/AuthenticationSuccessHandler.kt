package io.openfuture.api.config.handler

import io.openfuture.api.config.propety.AuthorizationProperties
import io.openfuture.api.entity.auth.User
import io.openfuture.api.service.OpenKeyService
import io.openfuture.api.service.UserService
import io.openfuture.api.util.CookieUtils
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.core.oidc.user.OidcUser
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class AuthenticationSuccessHandler(
        private val properties: AuthorizationProperties,
        private val userService: UserService,
        private val keyService: OpenKeyService
) : SavedRequestAwareAuthenticationSuccessHandler() {

    override fun onAuthenticationSuccess(request: HttpServletRequest, response: HttpServletResponse,
                                         authentication: Authentication) {
        val principal = authentication.principal as OidcUser
        val persistUser = userService.findByGoogleId(principal.subject) ?: userService.save(User(principal.subject))

        val key = persistUser.openKeys.firstOrNull() ?: keyService.generate(persistUser)
        CookieUtils.add(response, properties.cookieName!!, key.value)

        response.sendRedirect("/scaffolds")
    }

}