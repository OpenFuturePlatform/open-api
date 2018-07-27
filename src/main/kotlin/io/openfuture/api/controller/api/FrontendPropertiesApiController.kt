package io.openfuture.api.controller.api

import io.openfuture.api.component.web3.Web3Wrapper
import io.openfuture.api.config.propety.EthereumProperties
import io.openfuture.api.domain.property.FrontendPropertiesDto
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.web3j.spring.autoconfigure.Web3jProperties

@RestController
@RequestMapping("/api/properties")
class FrontendPropertiesApiController(
        private val web3: Web3Wrapper,
        private val properties: EthereumProperties,
        private val web3Properties: Web3jProperties
) {

    @GetMapping
    fun get(): FrontendPropertiesDto {
        val version = web3.getNetVersion()
        return FrontendPropertiesDto(web3Properties.clientAddress, version, properties.openTokenAddress!!,
                properties.getCredentials().address)
    }

}