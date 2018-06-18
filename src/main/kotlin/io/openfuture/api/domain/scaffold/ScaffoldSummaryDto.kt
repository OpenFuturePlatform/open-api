package io.openfuture.api.domain.scaffold

import java.math.BigDecimal
import java.math.BigInteger

data class ScaffoldSummaryDto(
        val abi: String,
        val description: String,
        val fiatAmount: String,
        val fiatCurrency: String,
        val amount: BigDecimal,
        val transactionIndex: BigInteger,
        val vendorAddress: String,
        val tokenBalance: BigInteger,
        val enabled: Boolean
)