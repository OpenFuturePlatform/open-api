package io.openfuture.api.controller.api

import io.openfuture.api.domain.scaffold.ScaffoldTemplateDto
import io.openfuture.api.service.ScaffoldTemplateService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/scaffolds/templates")
class ScaffoldTemplateApiController(
        private val service: ScaffoldTemplateService
) {

    @GetMapping
    fun getAll(): List<ScaffoldTemplateDto> = service.getAll().map { ScaffoldTemplateDto(it) }

}