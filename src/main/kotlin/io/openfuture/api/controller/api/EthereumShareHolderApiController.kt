package io.openfuture.api.controller.api

import io.openfuture.api.annotation.Address
import io.openfuture.api.annotation.CurrentUser
import io.openfuture.api.domain.holder.AddEthereumShareHolderRequest
import io.openfuture.api.domain.holder.UpdateEthereumShareHolderRequest
import io.openfuture.api.domain.scaffold.EthereumScaffoldSummaryDto
import io.openfuture.api.entity.auth.User
import io.openfuture.api.service.EthereumScaffoldService
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/api/ethereum-scaffolds/{address}/holders")
@Validated
class EthereumShareHolderApiController(
        private val service: EthereumScaffoldService
) {

    @PreAuthorize("hasRole('MASTER')")
    @PostMapping
    fun addShareHolder(@Valid @RequestBody request: AddEthereumShareHolderRequest, @CurrentUser user: User,
                       @PathVariable address: String): EthereumScaffoldSummaryDto {
        val summary = service.addShareHolder(address, user, request)
        return EthereumScaffoldSummaryDto(summary)
    }

    @PreAuthorize("hasRole('MASTER')")
    @PutMapping("/{holderAddress}")
    fun updateShareHolder(@Valid @RequestBody request: UpdateEthereumShareHolderRequest, @CurrentUser user: User,
                          @PathVariable address: String, @Address @PathVariable holderAddress: String): EthereumScaffoldSummaryDto {
        val summary = service.updateShareHolder(address, user, holderAddress, request)
        return EthereumScaffoldSummaryDto(summary)
    }

    @PreAuthorize("hasRole('MASTER')")
    @DeleteMapping("/{holderAddress}")
    fun removeShareHolder(@CurrentUser user: User, @PathVariable address: String,
                          @PathVariable @Address holderAddress: String): EthereumScaffoldSummaryDto {
        val summary = service.removeShareHolder(address, user, holderAddress)
        return EthereumScaffoldSummaryDto(summary)
    }

}