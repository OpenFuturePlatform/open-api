package io.openfuture.api.controller.api

import io.openfuture.api.annotation.CurrentUser
import io.openfuture.api.domain.PageRequest
import io.openfuture.api.domain.PageResponse
import io.openfuture.api.domain.scaffold.*
import io.openfuture.api.entity.auth.User
import io.openfuture.api.service.ScaffoldService
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/api/scaffolds")
class ScaffoldApiController(
        private val service: ScaffoldService
) {

    @GetMapping
    fun getAll(@CurrentUser user: User, pageRequest: PageRequest): PageResponse<ScaffoldDto> {
        val scaffolds = service.getAll(user, pageRequest).map { ScaffoldDto(it) }
        return PageResponse(scaffolds)
    }

    @GetMapping("/{address}")
    fun get(@CurrentUser user: User, @PathVariable address: String): ScaffoldDto {
        val scaffold = service.get(address, user)
        return ScaffoldDto(scaffold)
    }

    @PostMapping("/doCompile")
    fun compile(@Valid @RequestBody request: CompileScaffoldRequest): CompiledScaffoldDto =
            service.compile(request)

    @PreAuthorize("hasRole('DEPLOY')")
    @PostMapping("/doDeploy")
    fun deploy(@Valid @RequestBody request: DeployScaffoldRequest, @CurrentUser user: User): ScaffoldDto {
        val scaffold = service.deploy(request)
        return ScaffoldDto(scaffold)
    }

    @PostMapping
    fun save(@Valid @RequestBody request: SaveScaffoldRequest): ScaffoldDto {
        val scaffold = service.save(request)
        return ScaffoldDto(scaffold)
    }

    @PutMapping("/{address}")
    fun update(@Valid @RequestBody request: UpdateScaffoldRequest, @CurrentUser user: User,
               @PathVariable address: String): ScaffoldDto {
        val scaffold = service.update(address, user, request)
        return ScaffoldDto(scaffold)
    }

    @PatchMapping("/{address}")
    fun setWebHook(@Valid @RequestBody request: SetWebHookRequest, @CurrentUser user: User,
                   @PathVariable address: String): ScaffoldDto {
        val scaffold = service.setWebHook(address, request, user)
        return ScaffoldDto(scaffold)
    }

    @PreAuthorize("hasRole('DEPLOY')")
    @GetMapping("/{address}/summary")
    fun getScaffoldSummary(@CurrentUser user: User, @PathVariable address: String): ScaffoldSummaryDto {
        val summary = service.getScaffoldSummary(address, user)
        return ScaffoldSummaryDto(summary)
    }

    @PreAuthorize("hasRole('DEPLOY')")
    @PostMapping("/{address}/doDeactivate")
    fun deactivate(@CurrentUser user: User, @PathVariable address: String) {
        service.deactivate(address, user)
    }

    @GetMapping("/quota")
    fun getQuota(@CurrentUser user: User): ScaffoldQuotaDto = service.getQuota(user)

    @PreAuthorize("hasRole('DEPLOY')")
    @PostMapping("/{address}/holders")
    fun addShareHolder(@Valid @RequestBody request: AddShareHolderRequest, @CurrentUser user: User,
                       @PathVariable address: String) {
        service.addShareHolder(address, user, request)
    }

    @PreAuthorize("hasRole('DEPLOY')")
    @PutMapping("/{address}/holders")
    fun updateShareHolder(@Valid @RequestBody request: UpdateShareHolderRequest, @CurrentUser user: User,
                          @PathVariable address: String) {
        service.updateShareHolder(address, user, request)
    }

    @PreAuthorize("hasRole('DEPLOY')")
    @DeleteMapping("/{address}/holders")
    fun removeShareHolder(@Valid @RequestBody request: RemoveShareHolderRequest, @CurrentUser user: User,
                          @PathVariable address: String) {
        service.removeShareHolder(address, user, request)
    }

}