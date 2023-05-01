package io.openfuture.api.config.filter

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.openfuture.api.config.propety.AuthorizationProperties
import io.openfuture.api.domain.exception.ExceptionResponse
import io.openfuture.api.domain.state.WalletApiStateRequest
import io.openfuture.api.entity.application.Application
import io.openfuture.api.service.ApplicationService
import io.openfuture.api.util.*
import org.apache.commons.io.IOUtils
import org.springframework.http.HttpStatus.UNAUTHORIZED
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.util.ContentCachingRequestWrapper
import java.nio.charset.StandardCharsets
import java.util.*
import javax.servlet.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


class PublicApiAuthorizationFilter(
    private val applicationService: ApplicationService,
    private val mapper: ObjectMapper,
    private val properties: AuthorizationProperties
) : Filter {

    override fun init(filterConfig: FilterConfig?) {
        // Do nothing
    }

    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        request as HttpServletRequest
        response as HttpServletResponse

        if (request.requestURI.startsWith("/public") && request.getHeader("X-API-KEY") != null) {

            val accessKey = request.getHeader("X-API-KEY")
            val signature = request.getHeader("X-API-SIGNATURE")

            try {
                val application = applicationService.getByAccessKey(accessKey)

                if (request.method == "POST") {

                    val requestWrapper = CustomHttpRequestWrapper(request)

                    val timestamp = request.getHeader("X-API-TIMESTAMP")

                    val str1 = IOUtils.toString(requestWrapper.inputStream, StandardCharsets.UTF_8)

                    if (!checkHash(application, signature, str1, timestamp!!.toLong())) {
                        println("Signature mismatch or request timeout")
                        val exceptionResponse =
                            ExceptionResponse(UNAUTHORIZED.value(), "Signature mismatch or request timeout")
                        response.status = exceptionResponse.status
                        response.writer.write(mapper.writeValueAsString(exceptionResponse))
                        return
                    }

                    val token = UsernamePasswordAuthenticationToken(
                        application.user,
                        null,
                        listOf(SimpleGrantedAuthority("ROLE_APPLICATION"))
                    )
                    SecurityContextHolder.getContext().authentication = token

                    chain.doFilter(requestWrapper, response)
                    return
                } else {
                    val token = UsernamePasswordAuthenticationToken(
                        application.user,
                        null,
                        listOf(SimpleGrantedAuthority("ROLE_APPLICATION"))
                    )
                    SecurityContextHolder.getContext().authentication = token

                    chain.doFilter(request, response)
                    return
                }

            } catch (exception: RuntimeException) {
                val exceptionResponse = ExceptionResponse(UNAUTHORIZED.value(), exception.message!!)
                response.setContentType("application/json")
                response.status = exceptionResponse.status
                response.writer.write(mapper.writeValueAsString(exceptionResponse))
                return
            }

        }
        chain.doFilter(request, response)
    }

    override fun destroy() {
        // Do nothing
    }

    private fun checkHash(application: Application, signature: String, str: String, timestamp: Long): Boolean {

        val diffMinutes = differenceEpochs(currentEpochs(), timestamp)
        val expirePeriod = properties.expireApi!!

        val hmacSha256 = application.let {
            KeyGeneratorUtils.calcHmacSha256(it.apiSecretKey, str)
        }
        if (hmacSha256 != signature || diffMinutes > expirePeriod) {
            return false
        }
        return true
    }
}