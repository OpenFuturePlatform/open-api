package io.openfuture.api.domain.scaffold

import io.openfuture.api.entity.scaffold.ScaffoldSummary
import java.math.BigInteger

data class ScaffoldSummaryDto(
        val scaffold: ScaffoldDto,
        val transactionIndex: BigInteger,
        val tokenBalance: BigInteger,
        val enabled: Boolean
) {

    constructor(summary: ScaffoldSummary) : this(
            ScaffoldDto(summary.scaffold),
            summary.transactionIndex,
            summary.tokenBalance,
            summary.enabled
    )

}