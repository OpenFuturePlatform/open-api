package io.openfuture.api.controller.api

import io.openfuture.api.annotation.CurrentUser
import io.openfuture.api.domain.PageRequest
import io.openfuture.api.domain.PageResponse
import io.openfuture.api.domain.scaffold.*
import io.openfuture.api.entity.auth.User
import io.openfuture.api.service.ScaffoldService
import org.springframework.security.access.AccessDeniedException
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
    fun get(@PathVariable address: String): ScaffoldDto {
        val scaffold = service.get(address)
        return ScaffoldDto(scaffold)
    }

    @PostMapping("/doCompile")
    fun compile(@Valid @RequestBody request: CompileScaffoldRequest): CompiledScaffoldDto =
            service.compile(request)

    @PostMapping("/doDeploy")
    fun deploy(@CurrentUser user: User, @Valid @RequestBody request: DeployScaffoldRequest): ScaffoldDto {
        if (user.roles.none { it.key == "ROLE_DEPLOY" }) {
            throw AccessDeniedException("User not contain enough roles")
        }
        val scaffold = service.deploy(request)
        return ScaffoldDto(scaffold)
    }

    @PostMapping
    fun save(@Valid @RequestBody request: SaveScaffoldRequest): ScaffoldDto {
        val scaffold = service.save(request)
        return ScaffoldDto(scaffold)
    }

    @PatchMapping("/{address}")
    fun setWebHook(@Valid @RequestBody request: SetWebHookRequest, @PathVariable address: String): ScaffoldDto {
        val scaffold = service.setWebHook(address, request)
        return ScaffoldDto(scaffold)
    }

    @GetMapping("/{address}/summary")
    fun getScaffoldSummary(@PathVariable address: String): ScaffoldSummaryDto =
            service.getScaffoldSummary(address)

    @PostMapping("/{address}/doDeactivate")
    fun deactivate(@PathVariable address: String): ScaffoldSummaryDto =
            service.deactivate(address)

}