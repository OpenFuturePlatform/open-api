package io.openfuture.api.config

import io.openfuture.api.annotation.resolver.CurrentUserArgumentResolver
import io.openfuture.api.repository.UserRepository
import org.springframework.context.annotation.Configuration
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

/**
 * @author Kadach Alexey
 */
@Configuration
class WebMvcConfig(
        private val userRepository: UserRepository
) : WebMvcConfigurer {

    override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>) {
        resolvers.add(CurrentUserArgumentResolver(userRepository))
    }

}