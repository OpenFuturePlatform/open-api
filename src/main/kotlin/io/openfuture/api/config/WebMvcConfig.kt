package io.openfuture.api.config

import io.openfuture.api.annotation.resolver.CurrentTokenArgumentResolver
import io.openfuture.api.config.propety.AuthorizationProperties
import org.springframework.context.annotation.Configuration
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebMvcConfig(
        private val properties: AuthorizationProperties
) : WebMvcConfigurer {

    override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>) {
        resolvers.add(CurrentTokenArgumentResolver(properties))
    }

}