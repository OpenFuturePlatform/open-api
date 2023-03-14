package io.openfuture.api.config

import com.fasterxml.jackson.databind.ObjectMapper
import io.openfuture.api.config.filter.ApiAuthorizationFilter
import io.openfuture.api.config.filter.PublicApiAuthorizationFilter
import io.openfuture.api.config.handler.AuthenticationSuccessHandler
import io.openfuture.api.config.propety.AuthorizationProperties
import io.openfuture.api.service.ApplicationService
import io.openfuture.api.service.UserService
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
class SecurityConfig(
    private val userService: UserService,
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
                    .antMatchers("/api/application/**").permitAll()
                    .antMatchers("/api/token/**").permitAll()
            .antMatchers("/dummy/**", "/v2/wallet/**").anonymous()
                    //.antMatchers("/api/**").access("hasIpAddress('${properties.cidr}') or hasIpAddress('${properties.openState}') or hasIpAddress('127.0.0.1') or hasIpAddress('0:0:0:0:0:0:0:1')")
                    .anyRequest().authenticated()

            .and()

            .addFilterAfter(ApiAuthorizationFilter(mapper, properties), OAuth2LoginAuthenticationFilter::class.java)
            .addFilterAfter(PublicApiAuthorizationFilter(applicationService, mapper, properties), ApiAuthorizationFilter::class.java)

            .oauth2Login()
            .authorizationEndpoint()

            .and()

            .loginPage("/")
            .successHandler(AuthenticationSuccessHandler(userService))

            .and()

            .logout().invalidateHttpSession(true)
            .clearAuthentication(true)
            .logoutSuccessUrl("/")
            .deleteCookies("JSESSIONID").permitAll()

        // @formatter:on
    }

}