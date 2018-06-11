package io.openfuture.api.config

import io.openfuture.api.config.filters.AuthorizationFilter
import io.openfuture.api.config.handler.AuthenticationSuccessHandler
import io.openfuture.api.config.propety.AuthorizationProperties
import io.openfuture.api.service.OpenKeyService
import io.openfuture.api.service.UserService
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy.STATELESS
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter


/**
 * @author Kadach Alexey
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
class SecurityConfig(
        private val userService: UserService,
        private val keyService: OpenKeyService,
        private val properties: AuthorizationProperties
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

                .addFilterAfter(AuthorizationFilter(properties, keyService), OAuth2LoginAuthenticationFilter::class.java)
                .sessionManagement().sessionCreationPolicy(STATELESS)

                .and()

                .oauth2Login()
                    .authorizationEndpoint()
                    .authorizationRequestRepository(OAuth2AuthorizationRequestRepository(properties))

                .and()

                .loginPage("/")
                .successHandler(AuthenticationSuccessHandler(properties, userService))

                .and()

                .logout().disable()
        // @formatter:on
    }

}