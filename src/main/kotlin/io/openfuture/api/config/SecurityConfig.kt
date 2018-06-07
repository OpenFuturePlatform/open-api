package io.openfuture.api.config

import io.openfuture.api.config.filters.AuthorizationFilter
import io.openfuture.api.config.handler.AuthenticationSuccessHandler
import io.openfuture.api.config.propety.AuthorizationProperties
import io.openfuture.api.service.OpenKeyService
import io.openfuture.api.service.UserService
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter


/**
 * @author Kadach Alexey
 */
@Configuration
class SecurityConfig(
        private val userService: UserService,
        private val keyService: OpenKeyService,
        private val properites: AuthorizationProperties
) : WebSecurityConfigurerAdapter() {

    override fun configure(http: HttpSecurity) {
        http.csrf().disable()

        // @formatter:off
        http
                .authorizeRequests()
                    .antMatchers("/").permitAll()
                    .antMatchers("/css/**").permitAll()
                    .antMatchers("/img/**").permitAll()
                    .antMatchers("/static/**").permitAll()
                    .antMatchers("**.js").permitAll()
                    .anyRequest().authenticated()

                .and()

                .addFilterAfter(AuthorizationFilter(properites, keyService), OAuth2LoginAuthenticationFilter::class.java)
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()

                .oauth2Login()
                    .loginPage("/")
                    .successHandler(AuthenticationSuccessHandler(properites, userService))
        // @formatter:on
    }

}