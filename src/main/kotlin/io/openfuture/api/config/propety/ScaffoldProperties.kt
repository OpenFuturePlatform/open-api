package io.openfuture.api.config.propety

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component
import org.springframework.validation.annotation.Validated
import javax.validation.constraints.NotNull

@ConfigurationProperties(prefix = "scaffold")
@Validated
@Component
class ScaffoldProperties(
        @field:NotNull var allowedDisabledContracts: Int = 10,
        @field:NotNull var enabledContactTokenCount: Int = 10,
        @field:NotNull var cachePeriodInMinutest: Int = 1
)