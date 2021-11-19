package io.openfuture.api.config

import com.fasterxml.jackson.databind.ObjectMapper
import io.openfuture.api.config.filter.ApiAuthorizationFilter
import io.openfuture.api.config.filter.AuthorizationFilter
import io.openfuture.api.config.filter.PublicApiAuthorizationFilter
import io.openfuture.api.config.handler.AuthenticationSuccessHandler
import io.openfuture.api.config.propety.AuthorizationProperties
import io.openfuture.api.config.repository.OAuth2AuthorizationRequestRepository
import io.openfuture.api.service.ApplicationService
import io.openfuture.api.service.OpenKeyService
import io.openfuture.api.service.UserService
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy.STATELESS
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
class SecurityConfig(
        private val userService: UserService,
        private val keyService: OpenKeyService,
        private val applicationService: ApplicationService,
        private val properties: AuthorizationProperties,
        private val mapper: ObjectMapper
) : WebSecurityConfigurerAdapter() {

    override fun configure(http: HttpSecurity) {
        http.cors()
        http.csrf().disable()
        http.headers().frameOptions().disable()

        // @formatter:off
        http
                .authorizeRequests()
                    .antMatchers("/").permitAll()
                    .antMatchers("/css/**").permitAll()
                    .antMatchers("/img/**").permitAll()
                    .antMatchers("/static/**").permitAll()
                    .antMatchers("**.js").permitAll()
                    .antMatchers("/widget/**").permitAll()
                    .anyRequest().authenticated()

                .and()

                .addFilterAfter(AuthorizationFilter(properties, keyService), OAuth2LoginAuthenticationFilter::class.java)
                .addFilterAfter(ApiAuthorizationFilter(mapper), AuthorizationFilter::class.java)
                .addFilterAfter(PublicApiAuthorizationFilter(applicationService), AuthorizationFilter::class.java)
                .sessionManagement().sessionCreationPolicy(STATELESS)

                .and()

                .oauth2Login()
                    .authorizationEndpoint()
                    .authorizationRequestRepository(OAuth2AuthorizationRequestRepository(properties))

                    .and()

                    .loginPage("/")
                    .successHandler(AuthenticationSuccessHandler(properties, userService, keyService))

                .and()

                .logout().disable()
        // @formatter:on
    }

}