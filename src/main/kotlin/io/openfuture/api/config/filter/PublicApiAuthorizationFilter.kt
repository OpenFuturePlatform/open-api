package io.openfuture.api.config.filter

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.openfuture.api.config.propety.AuthorizationProperties
import org.springframework.http.HttpStatus.UNAUTHORIZED
import io.openfuture.api.domain.exception.ExceptionResponse
import io.openfuture.api.domain.key.WalletApiCreateRequest
import io.openfuture.api.domain.state.WalletApiStateRequest
import io.openfuture.api.service.ApplicationService
import io.openfuture.api.util.*
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
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

            val requestWrapper = CustomHttpRequestWrapper(request)
            val walletApiCreateRequest =
                mapper.readValue(requestWrapper.bodyInStringFormat, WalletApiCreateRequest::class.java)
            val mapper = jacksonObjectMapper()
            val str = mapper.writeValueAsString(walletApiCreateRequest)

            val application = applicationService.getByAccessKey(accessKey)

            if (!checkHash(accessKey, signature, walletApiCreateRequest.timestamp.toLong(), str)) {
                val exceptionResponse = ExceptionResponse(UNAUTHORIZED.value(), "Signature mismatch or request timeout")
                response.status = exceptionResponse.status
                response.writer.write(mapper.writeValueAsString(exceptionResponse))
                return
            }

            val token = UsernamePasswordAuthenticationToken(application.user, null, listOf(SimpleGrantedAuthority("ROLE_APPLICATION")))
            SecurityContextHolder.getContext().authentication = token

            chain.doFilter(requestWrapper, response)
            return
        }

        else if (request.requestURI.startsWith("/public") && request.getHeader("OPEN-API-KEY") != null) {

            val accessKey = request.getHeader("OPEN-API-KEY")
            val signature = request.getHeader("OPEN-API-SIGNATURE")

            val requestWrapper = CustomHttpRequestWrapper(request)
            val walletApiStateRequest =
                mapper.readValue(requestWrapper.bodyInStringFormat, WalletApiStateRequest::class.java)
            val mapper = jacksonObjectMapper()
            val str = mapper.writeValueAsString(walletApiStateRequest)

            val application = applicationService.getByAccessKey(accessKey)

            if (!checkHash(accessKey, signature, walletApiStateRequest.timestamp.toLong(), str)) {
                val exceptionResponse = ExceptionResponse(UNAUTHORIZED.value(), "Signature mismatch or request timeout")
                response.status = exceptionResponse.status
                response.writer.write(mapper.writeValueAsString(exceptionResponse))
                return
            }

            val token = UsernamePasswordAuthenticationToken(application.user, null, listOf(SimpleGrantedAuthority("ROLE_APPLICATION")))
            SecurityContextHolder.getContext().authentication = token

            chain.doFilter(requestWrapper, response)
            return
        }

        chain.doFilter(request, response)
    }

    override fun destroy() {
        // Do nothing
    }

    private fun checkHash(accessKey: String, signature: String, timestamp: Long, str: String): Boolean{
        val diffMinutes = differenceEpochs(currentEpochs(), timestamp)
        val expirePeriod = properties.expireApi!!

        val application = applicationService.getByAccessKey(accessKey)

        val hmacSha256 = application.let {
            KeyGeneratorUtils.calcHmacSha256(it.apiSecretKey, str)
        }

        if (hmacSha256 != signature || diffMinutes > expirePeriod) {
            return false
        }
        return true
    }
}