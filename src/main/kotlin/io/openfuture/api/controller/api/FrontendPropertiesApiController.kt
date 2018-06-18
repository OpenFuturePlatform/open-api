package io.openfuture.api.controller.api

import io.openfuture.api.domain.FrontendPropertiesDto
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.web3j.protocol.Web3j
import org.web3j.spring.autoconfigure.Web3jProperties

@RestController
@RequestMapping("/api/properties")
class FrontendPropertiesApiController(
        private val web3: Web3j,
        private val properties: Web3jProperties
) {

    @GetMapping
    fun get(): FrontendPropertiesDto {
        val version = web3.netVersion().send().netVersion
        return FrontendPropertiesDto(properties.clientAddress, version)
    }

}