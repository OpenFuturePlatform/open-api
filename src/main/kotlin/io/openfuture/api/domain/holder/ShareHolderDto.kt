package io.openfuture.api.domain.holder

import io.openfuture.api.entity.scaffold.ShareHolder

data class ShareHolderDto(
        val address: String,
        val percent: Int
) {

    constructor(shareHolder: ShareHolder) : this(shareHolder.address, shareHolder.percent)

}