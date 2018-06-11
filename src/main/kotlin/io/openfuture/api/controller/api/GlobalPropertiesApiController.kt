package io.openfuture.api.controller.api

import io.openfuture.api.config.propety.EthereumProperties
import io.openfuture.api.domain.GlobalPropertiesDto
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * @author Kadach Alexey
 */
@RestController
@RequestMapping("/api/properties")
class GlobalPropertiesApiController(
        private val properties: EthereumProperties
) {

    @GetMapping
    fun get(): GlobalPropertiesDto = GlobalPropertiesDto(properties.infura!!)

}