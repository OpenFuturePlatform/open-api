package io.openfuture.api.component.scaffold.processor

import io.openfuture.api.component.scaffold.compiler.VersionedScaffoldCompiler
import io.openfuture.api.component.web3.Web3Wrapper
import io.openfuture.api.config.propety.EthereumProperties
import io.openfuture.api.config.propety.ScaffoldProperties
import io.openfuture.api.domain.scaffold.CompiledScaffoldDto
import io.openfuture.api.domain.scaffold.DeployEthereumScaffoldRequest
import io.openfuture.api.domain.scaffold.EthereumScaffoldPropertyDto
import io.openfuture.api.entity.scaffold.EthereumScaffold
import io.openfuture.api.entity.scaffold.EthereumScaffoldSummary
import io.openfuture.api.entity.scaffold.EthereumShareHolder
import io.openfuture.api.entity.scaffold.ScaffoldVersion
import io.openfuture.api.util.EthereumUtils.toChecksumAddress
import org.web3j.abi.TypeReference
import org.web3j.abi.datatypes.Address
import org.web3j.abi.datatypes.Utf8String
import org.web3j.abi.datatypes.generated.Bytes32
import org.web3j.abi.datatypes.generated.Uint256
import org.web3j.utils.Convert
import java.math.BigInteger

abstract class BaseVersionedScaffoldProcessor(
        private val version: ScaffoldVersion,
        protected val compiler: VersionedScaffoldCompiler,
        protected val web3: Web3Wrapper,
        protected val properties: EthereumProperties,
        protected val scaffoldProperties: ScaffoldProperties
): VersionedScaffoldProcessor {

    companion object {
        const val GET_SCAFFOLD_SUMMARY_METHOD_NAME = "getScaffoldSummary"
        const val DEACTIVATE_SCAFFOLD_METHOD_NAME = "deactivate"
        const val ADD_SHARE_HOLDER_METHOD_NAME = "addShareHolder"
        const val UPDATE_SHARE_HOLDER_METHOD_NAME = "editShareHolder"
        const val REMOVE_SHARE_HOLDER_METHOD_NAME = "deleteShareHolder"
        const val GET_SHARE_HOLDER_NUMBER_METHOD_NAME = "getShareHolderCount"
        const val GET_SHARE_HOLDER_AT_INDEX_METHOD_NAME = "getShareHolderAddressAndShareAtIndex"
    }


    override fun getVersion(): ScaffoldVersion = version

    override fun compile(properties: List<EthereumScaffoldPropertyDto>): CompiledScaffoldDto =
            CompiledScaffoldDto(compiler.compile(properties))

    override fun deploy(data: String?, request: DeployEthereumScaffoldRequest): String {
        val credentials = properties.getCredentials()
        return web3.deploy(
                data,
                listOf(
                        Address(request.developerAddress),
                        Address(credentials.address),
                        Utf8String(request.fiatAmount),
                        Utf8String(request.currency!!.getValue()),
                        Uint256(Convert.toWei(request.conversionAmount, Convert.Unit.ETHER).toBigInteger())
                )
        )
    }

    override fun deactivate(ethereumScaffold: EthereumScaffold) {
        web3.callTransaction(DEACTIVATE_SCAFFOLD_METHOD_NAME, listOf(), listOf(), ethereumScaffold.address)
    }

    override fun addShareHolder(ethereumScaffold: EthereumScaffold, address: String, percent: Long) {
        web3.callTransaction(ADD_SHARE_HOLDER_METHOD_NAME, listOf(Address(address),
                Uint256(percent)), listOf(), ethereumScaffold.address)
    }

    override fun updateShareHolder(ethereumScaffold: EthereumScaffold, address: String, percent: Long) {
        web3.callTransaction(UPDATE_SHARE_HOLDER_METHOD_NAME, listOf(Address(address),
                Uint256(percent)), listOf(), ethereumScaffold.address)
    }

    override fun removeShareHolder(ethereumScaffold: EthereumScaffold, address: String) {
        web3.callTransaction(REMOVE_SHARE_HOLDER_METHOD_NAME, listOf(Address(address)), listOf(),
                ethereumScaffold.address)
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
                        object : TypeReference<Uint256>() {}
                ),
                ethereumScaffold.address
        )

        return EthereumScaffoldSummary(
                ethereumScaffold,
                result[3].value as BigInteger,
                result[5].value as BigInteger,
                result[5].value as BigInteger >= BigInteger.valueOf(scaffoldProperties.enabledContactTokenCount.toLong())
        )
    }

    override fun getShareHolders(summary: EthereumScaffoldSummary): List<EthereumShareHolder> {
        val countResult = web3.callFunction(
                GET_SHARE_HOLDER_NUMBER_METHOD_NAME,
                listOf(),
                listOf(object : TypeReference<Uint256>() {}),
                summary.ethereumScaffold.address
        )

        var count = 0
        if (countResult.isNotEmpty()) {
            count = (countResult[0].value as BigInteger).toInt()
        }

        val shareHolders = mutableListOf<EthereumShareHolder>()
        for (i in 0 until count) {
            val shareHolderResult = web3.callFunction(
                    GET_SHARE_HOLDER_AT_INDEX_METHOD_NAME,
                    listOf(Uint256(i.toLong())),
                    listOf(object : TypeReference<Address>() {}, object : TypeReference<Uint256>() {}),
                    summary.ethereumScaffold.address
            )
            shareHolders.add(EthereumShareHolder(
                    summary,
                    toChecksumAddress(shareHolderResult[0].value as String),
                    (shareHolderResult[1].value as BigInteger).toInt()
            ))
        }

        return shareHolders
    }

}