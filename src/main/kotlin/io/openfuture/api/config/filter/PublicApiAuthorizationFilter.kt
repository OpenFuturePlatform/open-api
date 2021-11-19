package io.openfuture.api.config.filter

import io.openfuture.api.service.ApplicationService
import io.openfuture.api.util.KeyGeneratorUtils
import io.openfuture.api.util.toEpochMillis
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit
import javax.servlet.*
import javax.servlet.http.HttpServletRequest
import kotlin.math.abs


class PublicApiAuthorizationFilter(
    private val applicationService: ApplicationService
): Filter {
    override fun init(filterConfig: FilterConfig?) {
        // Do nothing
    }

    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        request as HttpServletRequest
        val accessKey = request.getHeader("X-API-KEY")
        val signature = request.getHeader("X-API-SIGNATURE")

        val paramMap = request.parameterMap
        val joinedParams = paramMap.entries.joinToString(separator = "&") { (key, value) ->
            "${key}=${value}"
        }

        //val diffInMillies: Long = abs(LocalDateTime.now().toEpochMillis() - paramMap["timestamp"].toString().toLong())
        //val diffMinutes = TimeUnit.MINUTES.convert(diffInMillies, TimeUnit.MILLISECONDS)

        println("Joined Params $joinedParams")

        val application = applicationService.getByAccessKey(accessKey)

        val hmacSha256 = application.let {
            KeyGeneratorUtils.calcHmacSha256(it.apiSecretKey, joinedParams)
        }

        if (hmacSha256 == signature){
            val token = UsernamePasswordAuthenticationToken(application.user, null,
                application.user.roles.map { SimpleGrantedAuthority("ROLE_APPLICATION") })
            SecurityContextHolder.getContext().authentication = token
        }

        chain.doFilter(request, response)
    }

    override fun destroy() {
        // Do nothing
    }
}