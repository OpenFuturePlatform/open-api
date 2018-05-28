package io.zensoft.open.api.controller.api

import io.zensoft.open.api.annotation.CurrentUser
import io.zensoft.open.api.domain.PageRequest
import io.zensoft.open.api.domain.PageResponse
import io.zensoft.open.api.domain.scaffold.DeployScaffoldRequest
import io.zensoft.open.api.domain.scaffold.ScaffoldDto
import io.zensoft.open.api.model.User
import io.zensoft.open.api.service.ScaffoldService
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