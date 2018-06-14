package io.openfuture.api.controller.api

import io.openfuture.api.annotation.CurrentUser
import io.openfuture.api.domain.scaffold.SaveScaffoldTemplateRequest
import io.openfuture.api.domain.scaffold.ScaffoldTemplateDto
import io.openfuture.api.entity.auth.User
import io.openfuture.api.service.ScaffoldTemplateService
import org.springframework.web.bind.annotation.*

/**
 * @author Kadach Alexey
 */
@RestController
@RequestMapping("/api/scaffolds/templates")
class ScaffoldTemplateApiController(
        private val service: ScaffoldTemplateService
) {

    @GetMapping
    fun getAll(@CurrentUser user: User): List<ScaffoldTemplateDto> = service.getAll(user).map { ScaffoldTemplateDto(it) }

    @PostMapping
    fun save(@RequestBody request: SaveScaffoldTemplateRequest, @CurrentUser user: User): ScaffoldTemplateDto {
        val template = service.save(request, user)
        return ScaffoldTemplateDto(template)
    }

}