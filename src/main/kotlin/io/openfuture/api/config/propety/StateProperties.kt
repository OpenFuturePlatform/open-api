package io.openfuture.api.config.propety

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component
import org.springframework.validation.annotation.Validated
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@ConfigurationProperties(prefix = "state")
@Validated
@Component
data class StateProperties(@field:NotNull @field:NotBlank var baseUrl: String?)
