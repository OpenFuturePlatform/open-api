package io.zensoft.open.api.annotation.resolver

import io.zensoft.open.api.annotation.CurrentUser
import io.zensoft.open.api.repository.UserRepository
import org.springframework.core.MethodParameter
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.core.oidc.user.OidcUser
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer

/**
 * @author Kadach Alexey
 */
class CurrentUserArgumentResolver(
        private val userRepository: UserRepository
): HandlerMethodArgumentResolver {

    override fun supportsParameter(parameter: MethodParameter): Boolean =
            parameter.hasParameterAnnotation(CurrentUser::class.java)

    override fun resolveArgument(parameter: MethodParameter,
                                 mavContainer: ModelAndViewContainer?,
                                 webRequest: NativeWebRequest,
                                 binderFactory: WebDataBinderFactory?): Any? {
        val principal = SecurityContextHolder.getContext().authentication.principal as OidcUser
        return userRepository.findById(principal.subject).orElseGet(null)
    }

}