package io.openfuture.api.domain.scaffold

import java.math.BigInteger

/**
 * @author Kadach Alexey
 */
data class ScaffoldSummaryDto(
        val description: String,
        val balance: BigInteger,
        val fiatAmount: String,
        val fiatCurrency: String,
        val amount: BigInteger,
        val transactionIndex: BigInteger,
        val vendorAddress: String
)