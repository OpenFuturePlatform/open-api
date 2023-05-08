package io.openfuture.api.config

import io.openfuture.api.annotation.resolver.CurrentUserArgumentResolver
import io.openfuture.api.config.interceptor.AccessKeyHolder
import io.openfuture.api.config.interceptor.AccessKeyInterceptor
import io.openfuture.api.service.UserService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Scope
import org.springframework.context.annotation.ScopedProxyMode
import org.springframework.web.context.WebApplicationContext
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer


@Configuration
class WebMvcConfig(
    private val userService: UserService
) : WebMvcConfigurer {

    override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>) {
        resolvers.add(CurrentUserArgumentResolver(userService))
    }

    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**")
            .allowedOrigins("*")
            .allowedMethods("HEAD", "OPTIONS", "GET", "POST", "PUT", "PATCH", "DELETE")
            .maxAge(3600)
    }

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(accessKeyInterceptor())
    }

    @Bean
    fun accessKeyInterceptor(): AccessKeyInterceptor {
        return AccessKeyInterceptor(accessKeyHolder())
    }

    @Bean
    @Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
    fun accessKeyHolder(): AccessKeyHolder {
        return AccessKeyHolder("")
    }

}