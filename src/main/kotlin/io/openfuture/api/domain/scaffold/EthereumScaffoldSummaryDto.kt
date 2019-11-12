package io.openfuture.api.domain.scaffold

import io.openfuture.api.domain.holder.EthereumShareHolderDto
import io.openfuture.api.entity.scaffold.EthereumScaffoldSummary
import java.math.BigInteger

data class EthereumScaffoldSummaryDto(
        val scaffold: EthereumScaffoldDto,
        val transactionIndex: BigInteger,
        val tokenBalance: BigInteger,
        val enabled: Boolean,
        val shareHolders: List<EthereumShareHolderDto>
) {

    constructor(summary: EthereumScaffoldSummary) : this(
            EthereumScaffoldDto(summary.ethereumScaffold),
            summary.transactionIndex,
            summary.tokenBalance,
            summary.enabled,
            summary.ethereumShareHolders.map { EthereumShareHolderDto(it) }
    )

}