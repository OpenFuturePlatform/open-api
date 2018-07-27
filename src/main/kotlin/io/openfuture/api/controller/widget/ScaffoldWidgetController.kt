package io.openfuture.api.controller.widget

import io.openfuture.api.domain.scaffold.ScaffoldInfoDto
import io.openfuture.api.service.ScaffoldService
import org.springframework.web.bind.annotation.*

@CrossOrigin
@RestController
@RequestMapping("/widget/scaffolds")
class ScaffoldWidgetController(
        private val service: ScaffoldService
) {

    @GetMapping("/{address}")
    fun get(@PathVariable address: String): ScaffoldInfoDto {
        val scaffold = service.get(address)
        return ScaffoldInfoDto(scaffold)
    }

}