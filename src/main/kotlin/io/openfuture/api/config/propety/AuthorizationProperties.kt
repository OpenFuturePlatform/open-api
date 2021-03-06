package io.openfuture.api.config.propety

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component
import org.springframework.validation.annotation.Validated
import javax.validation.constraints.NotEmpty

@ConfigurationProperties(prefix = "auth")
@Validated
@Component
class AuthorizationProperties(
        @field:NotEmpty var cookieName: String? = null
)