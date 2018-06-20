package io.openfuture.api.config.propety

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component
import org.springframework.validation.annotation.Validated
import org.web3j.crypto.Credentials
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

@ConfigurationProperties(prefix = "ethereum")
@Validated
@Component
class EthereumProperties(
        @field:NotEmpty var privateKey: String? = null,
        @field:NotEmpty var openTokenAddress: String? = null,
        @field:NotNull var allowedDisabledContracts: Int = 10,
        @field:NotNull var enabledContactTokenCount: Int = 10,
        @field:NotNull var cachePeriodInMinutest: Int = 3
) {

    fun getCredentials(): Credentials = Credentials.create(privateKey)

}