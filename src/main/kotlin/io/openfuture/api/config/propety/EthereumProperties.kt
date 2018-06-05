package io.openfuture.api.config.propety

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component
import org.springframework.validation.annotation.Validated
import javax.validation.constraints.NotEmpty

/**
 * @author Kadach Alexey
 */
@ConfigurationProperties(prefix = "ethereum")
@Validated
@Component
class EthereumProperties(
        @field:NotEmpty var infura: String? = null,
        @field:NotEmpty var privateKey: String? = null,
        @field:NotEmpty var openTokenAddress: String? = null
)