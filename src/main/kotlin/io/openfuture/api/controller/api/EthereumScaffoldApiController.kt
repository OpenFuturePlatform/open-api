package io.openfuture.api.controller.api

import io.openfuture.api.annotation.CurrentUser
import io.openfuture.api.domain.PageRequest
import io.openfuture.api.domain.PageResponse
import io.openfuture.api.domain.scaffold.*
import io.openfuture.api.entity.auth.User
import io.openfuture.api.service.EthereumScaffoldService
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/api/ethereum-scaffolds")
class EthereumScaffoldApiController(
        private val service: EthereumScaffoldService
) {

    @GetMapping
    fun getAll(@CurrentUser user: User, pageRequest: PageRequest): PageResponse<EthereumScaffoldDto> {
        val scaffolds = service.getAll(user, pageRequest).map { EthereumScaffoldDto(it) }
        return PageResponse(scaffolds)
    }

    @GetMapping("/{address}")
    fun get(@CurrentUser user: User, @PathVariable address: String): EthereumScaffoldDto {
        val scaffold = service.get(address, user)
        return EthereumScaffoldDto(scaffold)
    }

    @PostMapping("/doCompile")
    fun compile(@Valid @RequestBody request: CompileEthereumScaffoldRequest, @CurrentUser user: User): CompiledScaffoldDto =
            service.compile(request, user)

    @PreAuthorize("hasRole('MASTER')")
    @PostMapping("/doDeploy")
    fun deploy(@Valid @RequestBody request: DeployEthereumScaffoldRequest, @CurrentUser user: User): EthereumScaffoldDto {
        val scaffold = service.deploy(request, user)
        return EthereumScaffoldDto(scaffold)
    }

    @PostMapping
    fun save(@Valid @RequestBody request: SaveEthereumScaffoldRequest, @CurrentUser user: User): EthereumScaffoldDto {
        val scaffold = service.save(request, user)
        return EthereumScaffoldDto(scaffold)
    }

    @PutMapping("/{address}")
    fun update(@Valid @RequestBody request: UpdateEthereumScaffoldRequest, @CurrentUser user: User,
               @PathVariable address: String): EthereumScaffoldDto {
        val scaffold = service.update(address, user, request)
        return EthereumScaffoldDto(scaffold)
    }

    @PatchMapping("/{address}")
    fun setWebHook(@Valid @RequestBody request: SetWebHookRequest, @CurrentUser user: User,
                   @PathVariable address: String): EthereumScaffoldDto {
        val scaffold = service.setWebHook(address, request, user)
        return EthereumScaffoldDto(scaffold)
    }

    @PreAuthorize("hasRole('MASTER')")
    @GetMapping("/{address}/summary")
    fun getScaffoldSummary(@CurrentUser user: User, @PathVariable address: String): EthereumScaffoldSummaryDto {
        val summary = service.getScaffoldSummary(address, user)
        return EthereumScaffoldSummaryDto(summary)
    }

    @PreAuthorize("hasRole('MASTER')")
    @DeleteMapping("/{address}")
    fun deactivate(@CurrentUser user: User, @PathVariable address: String): EthereumScaffoldSummaryDto {
        val summary = service.deactivate(address, user)
        return EthereumScaffoldSummaryDto(summary)
    }

    @PreAuthorize("hasRole('MASTER')")
    @PostMapping("/{address}")
    fun activate(@CurrentUser user: User, @PathVariable address: String): EthereumScaffoldSummaryDto {
        val summary = service.activate(address, user)
        return EthereumScaffoldSummaryDto(summary)
    }

    @GetMapping("/quota")
    fun getQuota(@CurrentUser user: User): EthereumScaffoldQuotaDto = service.getQuota(user)

}