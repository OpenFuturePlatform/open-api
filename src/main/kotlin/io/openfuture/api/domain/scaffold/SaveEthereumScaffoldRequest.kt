package io.openfuture.api.domain.scaffold

import io.openfuture.api.annotation.Address
import io.openfuture.api.entity.scaffold.Currency
import io.openfuture.api.entity.scaffold.ScaffoldVersion
import javax.validation.Valid
import javax.validation.constraints.*

data class SaveEthereumScaffoldRequest(
        @Address @field:NotBlank var address: String? = null,
        @field:NotBlank var abi: String? = null,
        @field:NotBlank var openKey: String? = null,
        @field:NotBlank @field:Address var developerAddress: String? = null,
        @field:NotBlank var description: String? = null,
        @field:NotBlank @field:Digits(integer = Int.MAX_VALUE, fraction = Int.MAX_VALUE) var fiatAmount: String? = null,
        @field:NotNull var currency: Currency? = null,
        @field:NotBlank @field:Digits(integer = Int.MAX_VALUE, fraction = Int.MAX_VALUE) var conversionAmount: String? = null,
        var webHook: String? = null,
        @field:NotEmpty @field:Size(max = 9) @field:Valid var properties: List<EthereumScaffoldPropertyDto> = listOf(),
        @field:NotNull var version: ScaffoldVersion = ScaffoldVersion.last()
)