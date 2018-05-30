package io.openfuture.api.controller.api

import io.openfuture.api.annotation.CurrentUser
import io.openfuture.api.domain.PageRequest
import io.openfuture.api.domain.PageResponse
import io.openfuture.api.domain.scaffold.DeployScaffoldRequest
import io.openfuture.api.domain.scaffold.ScaffoldDto
import io.openfuture.api.model.auth.User
import io.openfuture.api.service.ScaffoldService
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

/**
 * @author Kadach Alexey
 */
@RestController
@RequestMapping("/api/scaffolds")
class ScaffoldApiController(
        private val scaffoldService: ScaffoldService
) {

    @GetMapping
    fun getAll(@CurrentUser user: User, pageRequest: PageRequest): PageResponse<ScaffoldDto> {
        val scaffolds = scaffoldService.getAll(user, pageRequest).map { ScaffoldDto(it) }
        return PageResponse(scaffolds)
    }

    @GetMapping("/{address}")
    fun get(@PathVariable address: String): ScaffoldDto {
        val scaffold = scaffoldService.get(address)
        return ScaffoldDto(scaffold)
    }

    @PostMapping
    fun deploy(@Valid @RequestBody request: DeployScaffoldRequest, @CurrentUser user: User): ScaffoldDto {
        val scaffold = scaffoldService.deploy(request, user)
        return ScaffoldDto(scaffold)
    }

}