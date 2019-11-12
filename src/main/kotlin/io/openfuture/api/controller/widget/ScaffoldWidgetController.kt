package io.openfuture.api.controller.widget

import io.openfuture.api.domain.scaffold.EthereumScaffoldInfoDto
import io.openfuture.api.service.EthereumScaffoldService
import org.springframework.web.bind.annotation.*

@CrossOrigin
@RestController
@RequestMapping("/widget/scaffolds")
class ScaffoldWidgetController(
        private val service: EthereumScaffoldService
) {

    @GetMapping("/{address}")
    fun get(@PathVariable address: String): EthereumScaffoldInfoDto {
        val scaffold = service.get(address)
        return EthereumScaffoldInfoDto(scaffold)
    }

}