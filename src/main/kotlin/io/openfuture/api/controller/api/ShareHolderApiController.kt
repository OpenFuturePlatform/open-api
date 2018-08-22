package io.openfuture.api.controller.api

import io.openfuture.api.annotation.Address
import io.openfuture.api.annotation.CurrentUser
import io.openfuture.api.domain.holder.AddShareHolderRequest
import io.openfuture.api.domain.holder.UpdateShareHolderRequest
import io.openfuture.api.domain.scaffold.ScaffoldSummaryDto
import io.openfuture.api.entity.auth.User
import io.openfuture.api.service.ScaffoldService
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/api/scaffolds/{address}/holders")
@Validated
class ShareHolderApiController(
        private val service: ScaffoldService
) {

    @PreAuthorize("hasRole('MASTER')")
    @PostMapping
    fun addShareHolder(@Valid @RequestBody request: AddShareHolderRequest, @CurrentUser user: User,
                       @PathVariable address: String): ScaffoldSummaryDto {
        val summary = service.addShareHolder(address, user, request)
        return ScaffoldSummaryDto(summary)
    }

    @PreAuthorize("hasRole('MASTER')")
    @PutMapping("/{holderAddress}")
    fun updateShareHolder(@Valid @RequestBody request: UpdateShareHolderRequest, @CurrentUser user: User,
                          @PathVariable address: String, @Address @PathVariable holderAddress: String): ScaffoldSummaryDto {
        val summary = service.updateShareHolder(address, user, holderAddress, request)
        return ScaffoldSummaryDto(summary)
    }

    @PreAuthorize("hasRole('MASTER')")
    @DeleteMapping("/{holderAddress}")
    fun removeShareHolder(@CurrentUser user: User, @PathVariable address: String,
                          @PathVariable @Address holderAddress: String): ScaffoldSummaryDto {
        val summary = service.removeShareHolder(address, user, holderAddress)
        return ScaffoldSummaryDto(summary)
    }

}