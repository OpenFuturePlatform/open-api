package io.openfuture.api.controller.api

import io.openfuture.api.domain.FrontendPropertiesDto
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.web3j.spring.autoconfigure.Web3jProperties

/**
 * @author Kadach Alexey
 */
@RestController
@RequestMapping("/api/properties")
class FrontendPropertiesApiController(
        private val properties: Web3jProperties
) {

    @GetMapping
    fun get(): FrontendPropertiesDto = FrontendPropertiesDto(properties.clientAddress)

}