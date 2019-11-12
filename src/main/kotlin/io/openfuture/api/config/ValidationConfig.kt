package io.openfuture.api.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor

@Configuration
class ValidationConfig {

    @Bean
    fun methodValidationPostProcessor(): MethodValidationPostProcessor = MethodValidationPostProcessor()

}