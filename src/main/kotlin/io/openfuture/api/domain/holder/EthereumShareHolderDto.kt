package io.openfuture.api.domain.holder

import io.openfuture.api.entity.scaffold.EthereumShareHolder

data class EthereumShareHolderDto(
        val address: String,
        val percent: Int
) {

    constructor(ethereumShareHolder: EthereumShareHolder) : this(ethereumShareHolder.address, ethereumShareHolder.percent)

}