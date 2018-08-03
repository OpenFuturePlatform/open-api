package io.openfuture.api.config.propety

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component
import org.springframework.validation.annotation.Validated
import javax.validation.constraints.NotBlank

@ConfigurationProperties(prefix = "widget")
@Validated
@Component
class WidgetProperties(
    @field:NotBlank var host: String? = null
)