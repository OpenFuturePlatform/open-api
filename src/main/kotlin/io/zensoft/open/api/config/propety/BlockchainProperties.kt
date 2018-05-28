package io.zensoft.open.api.config.propety

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component
import org.springframework.validation.annotation.Validated
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

/**
 * @author Kadach Alexey
 */
@ConfigurationProperties(prefix = "blockchain")
@Validated
@Component
class BlockchainProperties(
        @field:NotNull var url: String? = null,
        @field:NotEmpty var baseAccount: String? = null
)