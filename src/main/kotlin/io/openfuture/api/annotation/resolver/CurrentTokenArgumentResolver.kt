package io.openfuture.api.annotation.resolver

import io.openfuture.api.annotation.CurrentToken
import io.openfuture.api.config.propety.AuthorizationProperties
import org.springframework.core.MethodParameter
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer
import javax.servlet.http.HttpServletRequest

class CurrentTokenArgumentResolver(
        private val properties: AuthorizationProperties
) : HandlerMethodArgumentResolver {

    override fun supportsParameter(parameter: MethodParameter): Boolean =
            parameter.hasParameterAnnotation(CurrentToken::class.java)

    override fun resolveArgument(parameter: MethodParameter,
                                 mavContainer: ModelAndViewContainer?,
                                 webRequest: NativeWebRequest,
                                 binderFactory: WebDataBinderFactory?): Any? {
        val request = webRequest.nativeRequest as HttpServletRequest
        return request.getHeader(AUTHORIZATION)
                ?: request.cookies?.firstOrNull { properties.cookieName == it.name }?.value
    }

}