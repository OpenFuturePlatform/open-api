package io.openfuture.api.component.scaffold.processor

import io.openfuture.api.component.scaffold.compiler.VersionedScaffoldCompiler
import io.openfuture.api.component.web3.Web3Wrapper
import io.openfuture.api.config.propety.EthereumProperties
import io.openfuture.api.config.propety.ScaffoldProperties
import io.openfuture.api.domain.scaffold.CompiledScaffoldDto
import io.openfuture.api.domain.scaffold.DeployScaffoldRequest
import io.openfuture.api.domain.scaffold.ScaffoldPropertyDto
import io.openfuture.api.entity.scaffold.Scaffold
import io.openfuture.api.entity.scaffold.ScaffoldSummary
import io.openfuture.api.entity.scaffold.ScaffoldVersion
import io.openfuture.api.entity.scaffold.ShareHolder
import org.web3j.abi.TypeReference
import org.web3j.abi.datatypes.Address
import org.web3j.abi.datatypes.Utf8String
import org.web3j.abi.datatypes.generated.Bytes32
import org.web3j.abi.datatypes.generated.Uint256
import org.web3j.utils.Convert
import java.math.BigInteger

abstract class BaseVersionedScaffoldProcessor(
        private val version: ScaffoldVersion,
        private val compiler: VersionedScaffoldCompiler,
        private val web3: Web3Wrapper,
        private val properties: EthereumProperties,
        private val scaffoldProperties: ScaffoldProperties
): VersionedScaffoldProcessor {

    companion object {
        private const val GET_SCAFFOLD_SUMMARY_METHOD_NAME = "getScaffoldSummary"
        private const val DEACTIVATE_SCAFFOLD_METHOD_NAME = "deactivate"
        private const val ADD_SHARE_HOLDER_METHOD_NAME = "addShareHolder"
        private const val UPDATE_SHARE_HOLDER_METHOD_NAME = "editShareHolder"
        private const val REMOVE_SHARE_HOLDER_METHOD_NAME = "deleteShareHolder"
        private const val GET_SHARE_HOLDER_NUMBER_METHOD_NAME = "getShareHolderCount"
        private const val GET_SHARE_HOLDER_AT_INDEX_METHOD_NAME = "getShareHolderAddressAndShareAtIndex"
    }


    override fun getVersion(): ScaffoldVersion = version

    override fun compile(properties: List<ScaffoldPropertyDto>): CompiledScaffoldDto =
            CompiledScaffoldDto(compiler.compile(properties))

    override fun deploy(data: String, request: DeployScaffoldRequest): String {
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

    override fun deactivate(scaffold: Scaffold) {
        web3.callTransaction(DEACTIVATE_SCAFFOLD_METHOD_NAME, listOf(), listOf(), scaffold.address)
    }

    override fun addShareHolder(scaffold: Scaffold, address: String, percent: Long) {
        web3.callTransaction(ADD_SHARE_HOLDER_METHOD_NAME, listOf(Address(address),
                Uint256(percent)), listOf(), scaffold.address)
    }

    override fun updateShareHolder(scaffold: Scaffold, address: String, percent: Long) {
        web3.callTransaction(UPDATE_SHARE_HOLDER_METHOD_NAME, listOf(Address(address),
                Uint256(percent)), listOf(), scaffold.address)
    }

    override fun removeShareHolder(scaffold: Scaffold, address: String) {
        web3.callTransaction(REMOVE_SHARE_HOLDER_METHOD_NAME, listOf(Address(address)), listOf(),
                scaffold.address)
    }

    override fun getScaffoldSummary(scaffold: Scaffold): ScaffoldSummary {
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
                scaffold.address
        )

        return ScaffoldSummary(
                scaffold,
                result[3].value as BigInteger,
                result[5].value as BigInteger,
                result[5].value as BigInteger >= BigInteger.valueOf(scaffoldProperties.enabledContactTokenCount.toLong())
        )
    }

    override fun getShareHolders(summary: ScaffoldSummary): List<ShareHolder> {
        val countResult = web3.callFunction(
                GET_SHARE_HOLDER_NUMBER_METHOD_NAME,
                listOf(),
                listOf(object : TypeReference<Uint256>() {}),
                summary.scaffold.address
        )
        val count = (countResult[0].value as BigInteger).toInt()

        val shareHolders = mutableListOf<ShareHolder>()
        for (i in 0 until count) {
            val shareHolderResult = web3.callFunction(
                    GET_SHARE_HOLDER_AT_INDEX_METHOD_NAME,
                    listOf(Uint256(i.toLong())),
                    listOf(object : TypeReference<Address>() {}, object : TypeReference<Uint256>() {}),
                    summary.scaffold.address
            )
            shareHolders.add(ShareHolder(
                    summary,
                    shareHolderResult[0].value as String,
                    (shareHolderResult[1].value as BigInteger).toInt()
            ))
        }

        return shareHolders
    }

}