package io.openfuture.api.config

import io.openfuture.api.annotation.resolver.CurrentUserArgumentResolver
import io.openfuture.api.service.UserService
import org.springframework.context.annotation.Configuration
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebMvcConfig(
    private val userService: UserService
) : WebMvcConfigurer {

    override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>) {
        resolvers.add(CurrentUserArgumentResolver(userService))
    }

}