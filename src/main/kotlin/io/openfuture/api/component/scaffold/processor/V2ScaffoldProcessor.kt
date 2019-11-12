package io.openfuture.api.component.scaffold.processor

import io.openfuture.api.component.scaffold.compiler.V2EthereumScaffoldCompiler
import io.openfuture.api.component.web3.Web3Wrapper
import io.openfuture.api.config.propety.EthereumProperties
import io.openfuture.api.config.propety.ScaffoldProperties
import io.openfuture.api.entity.scaffold.EthereumScaffold
import io.openfuture.api.entity.scaffold.EthereumScaffoldSummary
import io.openfuture.api.entity.scaffold.ScaffoldVersion.V2
import org.springframework.stereotype.Component
import org.web3j.abi.TypeReference
import org.web3j.abi.datatypes.Address
import org.web3j.abi.datatypes.Bool
import org.web3j.abi.datatypes.generated.Bytes32
import org.web3j.abi.datatypes.generated.Uint256
import java.math.BigInteger

@Component
class V2ScaffoldProcessor(
        web3: Web3Wrapper,
        compiler: V2EthereumScaffoldCompiler,
        properties: EthereumProperties,
        scaffoldProperties: ScaffoldProperties
) : BaseVersionedScaffoldProcessor(V2, compiler, web3, properties, scaffoldProperties) {

    companion object {
        const val ACTIVATE_SCAFFOLD_METHOD_NAME = "activate"
    }


    override fun activate(ethereumScaffold: EthereumScaffold) {
        web3.callTransaction(ACTIVATE_SCAFFOLD_METHOD_NAME, listOf(), listOf(), ethereumScaffold.address)
    }

    override fun getScaffoldSummary(ethereumScaffold: EthereumScaffold): EthereumScaffoldSummary {
        val result = web3.callFunction(
                GET_SCAFFOLD_SUMMARY_METHOD_NAME,
                listOf(),
                listOf(
                        object : TypeReference<Bytes32>() {},
                        object : TypeReference<Bytes32>() {},
                        object : TypeReference<Uint256>() {},
                        object : TypeReference<Uint256>() {},
                        object : TypeReference<Address>() {},
                        object : TypeReference<Uint256>() {},
                        object : TypeReference<Bool>() {}
                ),
                ethereumScaffold.address
        )

        if(result.isEmpty()) return EthereumScaffoldSummary(ethereumScaffold, BigInteger.ZERO, BigInteger.ZERO, false)

        return EthereumScaffoldSummary(
                ethereumScaffold,
                result[3].value as BigInteger,
                result[5].value as BigInteger,
                result[6].value as Boolean
        )
    }
}