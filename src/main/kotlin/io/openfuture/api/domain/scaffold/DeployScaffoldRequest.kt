package io.openfuture.api.domain.scaffold

import javax.validation.constraints.Digits
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

/**
 * @author Kadach Alexey
 */
data class DeployScaffoldRequest(
        @field:NotBlank var openKey: String? = null,
        @field:NotBlank var developerAddress: String? = null,
        @field:NotBlank var scaffoldDescription: String? = null,
        @field:NotBlank @field:Digits(integer = Int.MAX_VALUE, fraction = Int.MAX_VALUE) var fiatAmount: String? = null,
        @field:NotNull var conversionCurrency: CurrencyType? = null,
        @field:NotBlank @field:Digits(integer = Int.MAX_VALUE, fraction = Int.MAX_VALUE) var currencyConversionValue: String? = null,
        @field:NotNull var scaffoldFields: List<ScaffoldPropertyDto> = listOf()
)

enum class CurrencyType(private val value: String) {

    USD("usd"),
    GBP("gpb"),
    EUR("eur"),
    CNY("cny"),
    ETH("eth")
    ;

    fun getValue(): String = value

}