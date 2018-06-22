package io.openfuture.api.config.filter

import io.openfuture.api.config.propety.AuthorizationProperties
import io.openfuture.api.service.OpenKeyService
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import javax.servlet.*
import javax.servlet.http.HttpServletRequest

class AuthorizationFilter(
        private val properties: AuthorizationProperties,
        private val keyService: OpenKeyService
) : Filter {

    override fun init(filterConfig: FilterConfig?) {
        // Do nothing
    }

    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        request as HttpServletRequest

        val key = request.getHeader(AUTHORIZATION)
                ?: request.cookies?.firstOrNull { properties.cookieName == it.name }?.value

        val openKey = key?.let { keyService.find(it) }
        openKey?.let {
            val token = UsernamePasswordAuthenticationToken(openKey.user, null,
                    openKey.user.roles.map { SimpleGrantedAuthority(it.key) })
            SecurityContextHolder.getContext().authentication = token
        }

        chain.doFilter(request, response)
    }

    override fun destroy() {
        // Don nothing
    }

}