package io.openfuture.api.controller.api

import io.openfuture.api.annotation.CurrentUser
import io.openfuture.api.domain.PageRequest
import io.openfuture.api.domain.PageResponse
import io.openfuture.api.domain.scaffold.OpenScaffoldDto
import io.openfuture.api.domain.scaffold.SaveOpenScaffoldRequest
import io.openfuture.api.entity.auth.User
import io.openfuture.api.service.OpenScaffoldService
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/api/open-scaffolds")
class OpenScaffoldApiController(
        private val service: OpenScaffoldService
) {

    @GetMapping
    fun getAll(@CurrentUser user: User, pageRequest: PageRequest): PageResponse<OpenScaffoldDto> {
        val scaffolds = service.getAll(user, pageRequest).map { OpenScaffoldDto(it) }
        return PageResponse(scaffolds)
    }

    @PostMapping
    fun save(@Valid @RequestBody request: SaveOpenScaffoldRequest): OpenScaffoldDto {
        return OpenScaffoldDto(service.save(request))
    }

}