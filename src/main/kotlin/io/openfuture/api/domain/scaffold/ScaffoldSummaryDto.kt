package io.openfuture.api.domain.scaffold

import java.math.BigDecimal
import java.math.BigInteger

/**
 * @author Kadach Alexey
 */
data class ScaffoldSummaryDto(
        val description: String,
        val fiatAmount: String,
        val fiatCurrency: String,
        val amount: BigDecimal,
        val transactionIndex: BigInteger,
        val vendorAddress: String
)