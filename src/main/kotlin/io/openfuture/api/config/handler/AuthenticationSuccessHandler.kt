package io.openfuture.api.config.handler

import io.openfuture.api.config.OAuth2AuthorizationRequestRepository
import io.openfuture.api.config.propety.AuthorizationProperties
import io.openfuture.api.entity.auth.User
import io.openfuture.api.service.UserService
import io.openfuture.api.util.CookieUtils
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.core.oidc.user.OidcUser
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * @author Kadach Alexey
 */
class AuthenticationSuccessHandler(
        private val properties: AuthorizationProperties,
        private val service: UserService
) : SavedRequestAwareAuthenticationSuccessHandler() {

    override fun onAuthenticationSuccess(request: HttpServletRequest, response: HttpServletResponse,
                                         authentication: Authentication) {
        OAuth2AuthorizationRequestRepository.deleteAuthCookies(request, response)

        val principal = authentication.principal as OidcUser
        val persistUser = service.findByGoogleId(principal.subject) ?: service.save(User(principal.subject))

        CookieUtils.add(properties.cookieName!!, persistUser.openKeys.first().value, response)

        response.sendRedirect("/scaffolds")
    }

}