package io.openfuture.api.config.filter

import com.fasterxml.jackson.databind.ObjectMapper
import io.openfuture.api.domain.exception.ExceptionResponse
import org.springframework.http.HttpStatus.UNAUTHORIZED
import org.springframework.security.core.context.SecurityContextHolder
import javax.servlet.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class ApiAuthorizationFilter(private val mapper: ObjectMapper): Filter {

    override fun init(filterConfig: FilterConfig?) {
        // Do nothing
    }

    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        request as HttpServletRequest
        response as HttpServletResponse

        if (request.requestURI.startsWith("/api") && null == SecurityContextHolder.getContext().authentication) {
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

}