package io.openfuture.api.config

import io.openfuture.api.config.propety.AuthorizationProperties
import io.openfuture.api.util.CookieUtils
import org.apache.commons.lang3.SerializationUtils
import org.apache.commons.lang3.StringUtils
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest
import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * @author Alexey Skadorva
 */

class OAuth2AuthorizationRequestRepository(private val properties: AuthorizationProperties) : AuthorizationRequestRepository<OAuth2AuthorizationRequest> {

    companion object {
        const val SESSION_COOKIE_NAME = "auth_request"

        fun deleteAuthCookies(request: HttpServletRequest, response: HttpServletResponse) {
            val isSessionCookieExists = request.cookies.any({ SESSION_COOKIE_NAME == it.name })

            if (isSessionCookieExists) {
                CookieUtils.expire(SESSION_COOKIE_NAME, StringUtils.EMPTY, response)
            }
        }
    }

    override fun loadAuthorizationRequest(request: HttpServletRequest): OAuth2AuthorizationRequest {
        val authCookie = request.cookies.find({ it.name == SESSION_COOKIE_NAME })

        return SerializationUtils.deserialize(Base64.getUrlDecoder().decode(authCookie?.value))
    }

    override fun saveAuthorizationRequest(authorizationRequest: OAuth2AuthorizationRequest, request: HttpServletRequest,
                                          response: HttpServletResponse) {
        val serializedAuthorizationRequest = Base64.getUrlEncoder().encodeToString(SerializationUtils.serialize(authorizationRequest))

        CookieUtils.add(SESSION_COOKIE_NAME, serializedAuthorizationRequest, response)
    }

    override fun removeAuthorizationRequest(request: HttpServletRequest): OAuth2AuthorizationRequest {
        return loadAuthorizationRequest(request)
    }

}
