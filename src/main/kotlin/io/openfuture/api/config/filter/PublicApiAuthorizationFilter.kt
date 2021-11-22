package io.openfuture.api.config.filter

import com.fasterxml.jackson.databind.ObjectMapper
import io.openfuture.api.config.propety.AuthorizationProperties
import io.openfuture.api.domain.key.WalletApiCreateRequest
import io.openfuture.api.service.ApplicationService
import io.openfuture.api.util.CustomHttpRequestWrapper
import io.openfuture.api.util.KeyGeneratorUtils
import io.openfuture.api.util.differenceEpochs
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import javax.servlet.*
import javax.servlet.http.HttpServletRequest


class PublicApiAuthorizationFilter(
    private val applicationService: ApplicationService
): Filter {

    override fun init(filterConfig: FilterConfig?) {
        // Do nothing
    }

    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        request as HttpServletRequest

        if (request.requestURI.startsWith("/public") && null == SecurityContextHolder.getContext().authentication && request.getHeader("X-API-KEY") != null) {

            val accessKey = request.getHeader("X-API-KEY")
            val signature = request.getHeader("X-API-SIGNATURE")

            val requestWrapper = CustomHttpRequestWrapper(request)
            val walletApiCreateRequest = ObjectMapper().readValue(requestWrapper.bodyInStringFormat, WalletApiCreateRequest::class.java)

            val diffMinutes = differenceEpochs(System.currentTimeMillis() / 1000L, walletApiCreateRequest.timestamp.toLong())

            val application = applicationService.getByAccessKey(accessKey)

            val hmacSha256 = application.let {
                KeyGeneratorUtils.calcHmacSha256(it.apiSecretKey, walletApiCreateRequest.toString())
            }

            if (hmacSha256 == signature && diffMinutes < 10){
                val token = UsernamePasswordAuthenticationToken(application.user, null,
                    application.user.roles.map { SimpleGrantedAuthority("ROLE_APPLICATION") })
                SecurityContextHolder.getContext().authentication = token
            }

            chain.doFilter(requestWrapper, response);
            return
        }


        chain.doFilter(request, response)
    }

    override fun destroy() {
        // Do nothing
    }
}