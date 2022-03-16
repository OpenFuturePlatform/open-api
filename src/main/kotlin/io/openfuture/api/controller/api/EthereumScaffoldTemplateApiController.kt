package io.openfuture.api.controller.api

import io.openfuture.api.domain.scaffold.EthereumScaffoldTemplateDto
import io.openfuture.api.service.EthereumScaffoldTemplateService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/ethereum-scaffolds/templates")
class EthereumScaffoldTemplateApiController(
    private val service: EthereumScaffoldTemplateService
) {

    @GetMapping
    fun getAll(): List<EthereumScaffoldTemplateDto> = service.getAll().map { EthereumScaffoldTemplateDto(it) }

}