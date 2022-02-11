package io.openfuture.api.config.propety

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component
import org.springframework.validation.annotation.Validated

@ConfigurationProperties(prefix = "auth")
@Validated
@Component
class AuthorizationProperties(
        var expireApi: Long? = 10,
        var cidr: String? = null,
        var openState: String? = null,
        var allowLocalHost: Boolean = false
)