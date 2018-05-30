package io.openfuture.api.domain.scaffold

import io.openfuture.api.entity.scaffold.Currency
import javax.validation.Valid
import javax.validation.constraints.Digits
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

/**
 * @author Kadach Alexey
 */
data class DeployScaffoldRequest(
        @field:NotBlank var openKey: String? = null,
        @field:NotBlank var developerAddress: String? = null,
        @field:NotBlank var description: String? = null,
        @field:NotBlank @field:Digits(integer = Int.MAX_VALUE, fraction = Int.MAX_VALUE) var fiatAmount: String? = null,
        @field:NotNull var currency: Currency? = null,
        @field:NotBlank @field:Digits(integer = Int.MAX_VALUE, fraction = Int.MAX_VALUE) var conversionAmount: String? = null,
        @field:NotEmpty @field:Valid var properties: List<ScaffoldPropertyDto> = listOf()
)