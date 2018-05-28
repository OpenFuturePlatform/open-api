package io.openfuture.api.config

import io.openfuture.api.config.handler.AuthenticationSuccessHandler
import io.openfuture.api.service.UserService
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter


/**
 * @author Kadach Alexey
 */
@Configuration
class SecurityConfig(
        private val service: UserService
) : WebSecurityConfigurerAdapter() {

    override fun configure(http: HttpSecurity) {
        http
                .authorizeRequests()
                .anyRequest().authenticated()

                .and()

                .oauth2Login()
                .successHandler(AuthenticationSuccessHandler(service))
    }

}