package io.openfuture.api.config

import io.openfuture.api.config.propety.StateProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.DefaultUriBuilderFactory

@Configuration
class StateConfig {

    @Bean
    fun stateRestTemplate(stateProperties: StateProperties): RestTemplate {
        val restTemplate = RestTemplate()
        restTemplate.uriTemplateHandler = DefaultUriBuilderFactory(stateProperties.baseUrl!!)
        return restTemplate
    }

}
