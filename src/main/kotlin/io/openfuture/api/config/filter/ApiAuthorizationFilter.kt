package io.openfuture.api.config.filter

import com.fasterxml.jackson.databind.ObjectMapper
import io.openfuture.api.config.propety.AuthorizationProperties
import io.openfuture.api.domain.exception.ExceptionResponse
import org.springframework.http.HttpStatus.UNAUTHORIZED
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.util.matcher.IpAddressMatcher
import javax.servlet.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class ApiAuthorizationFilter(
    private val mapper: ObjectMapper,
    private val properties: AuthorizationProperties
): Filter {

    private val ipV4LoopBack = "127.0.0.1"
    private val ipV6LoopBack = "0:0:0:0:0:0:0:1"

    override fun init(filterConfig: FilterConfig?) {
        // Do nothing
    }

    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        request as HttpServletRequest
        response as HttpServletResponse

        if (request.requestURI.startsWith("/api") && null == SecurityContextHolder.getContext().authentication && !isAllowed(request)) {
            val exceptionResponse = ExceptionResponse(UNAUTHORIZED.value(), "Open token is invalid or disabled")
            response.status = exceptionResponse.status
            response.writer.write(mapper.writeValueAsString(exceptionResponse))
            return
        }

        chain.doFilter(request, response)
    }

    override fun destroy() {
        // Do nothing
    }

    fun isAllowed(request: HttpServletRequest): Boolean {

        val ip = request.remoteAddr
        if (properties.allowLocalHost && (ipV4LoopBack == ip || ipV6LoopBack == ip)) {
            return true
        }

        if (properties.cidr !=  null) {
            val matcher = IpAddressMatcher(properties.cidr)

            if (matcher.matches(request.getHeader("X-Forwarded-For"))) {
                return true
            }
        }

        return false
    }

}