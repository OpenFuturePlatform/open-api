package io.openfuture.api.domain.state

import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

data class DeleteWalletRequest(
        @field:NotNull var accountId: Long,
        @field:NotBlank var address: String,
        @field:NotNull var blockchainId: Int
)
