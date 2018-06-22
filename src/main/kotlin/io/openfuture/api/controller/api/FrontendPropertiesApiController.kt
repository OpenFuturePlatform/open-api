package io.openfuture.api.controller.api

import io.openfuture.api.component.web3.Web3Wrapper
import io.openfuture.api.domain.FrontendPropertiesDto
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.web3j.spring.autoconfigure.Web3jProperties

@RestController
@RequestMapping("/api/properties")
class FrontendPropertiesApiController(
        private val web3: Web3Wrapper,
        private val properties: Web3jProperties
) {

    @GetMapping
    fun get(): FrontendPropertiesDto {
        val version = web3.getNetVersion()
        return FrontendPropertiesDto(properties.clientAddress, version)
    }

}