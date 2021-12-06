package io.openfuture.api.config

import io.openfuture.api.config.propety.KeyProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.DefaultUriBuilderFactory

@Configuration
class KeyConfig {
    @Bean
    fun keyRestTemplate(keyProperties: KeyProperties): RestTemplate {
        val restTemplate = RestTemplate()
        restTemplate.uriTemplateHandler = DefaultUriBuilderFactory(keyProperties.baseUrl!!)
        return restTemplate
    }
}