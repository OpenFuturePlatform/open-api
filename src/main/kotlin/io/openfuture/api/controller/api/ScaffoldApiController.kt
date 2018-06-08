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

/**
 * @author Kadach Alexey
 */
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
    fun deploy(@CurrentUser user: User, @Valid @RequestBody request: DeployScaffoldRequest): ScaffoldDto {
        val scaffold = service.deploy(request)
        return ScaffoldDto(scaffold)
    }

    @PostMapping
    fun save(@Valid @RequestBody request: SaveScaffoldRequest): ScaffoldDto {
        val scaffold = service.save(request)
        return ScaffoldDto(scaffold)
    }

    @PatchMapping("/{address}")
    fun setWebHook(@CurrentUser user: User, @Valid @RequestBody request: SetWebHookRequest,
                   @PathVariable address: String): ScaffoldDto {
        val scaffold = service.setWebHook(address, request, user)
        return ScaffoldDto(scaffold)
    }

    @GetMapping("/{address}/summary")
    fun getScaffoldSummary(@CurrentUser user: User, @PathVariable address: String): ScaffoldSummaryDto =
            service.getScaffoldSummary(address, user)

    @PostMapping("/{address}/doDeactivate")
    fun deactivate(@CurrentUser user: User, @PathVariable address: String): ScaffoldSummaryDto =
            service.deactivate(address, user)

    @GetMapping("/quota")
    fun getQuota(@CurrentUser user: User): ScaffoldQuotaDto = service.getQuota(user)

}