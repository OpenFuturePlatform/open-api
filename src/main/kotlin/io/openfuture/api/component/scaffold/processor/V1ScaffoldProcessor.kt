package io.openfuture.api.component.scaffold.processor

import io.openfuture.api.component.scaffold.compiler.V1ScaffoldCompiler
import io.openfuture.api.component.web3.Web3Wrapper
import io.openfuture.api.config.propety.EthereumProperties
import io.openfuture.api.config.propety.ScaffoldProperties
import io.openfuture.api.entity.scaffold.Scaffold
import io.openfuture.api.entity.scaffold.ScaffoldVersion.V1
import org.springframework.stereotype.Component

@Component
class V1ScaffoldProcessor(
        web3: Web3Wrapper,
        compiler: V1ScaffoldCompiler,
        properties: EthereumProperties,
        scaffoldProperties: ScaffoldProperties
) : BaseVersionedScaffoldProcessor(V1, compiler, web3, properties, scaffoldProperties) {

    override fun activate(scaffold: Scaffold) {
        throw IllegalStateException("Scaffold with version ${getVersion()} hasn't method activate")
    }

}