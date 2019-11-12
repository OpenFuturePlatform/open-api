package io.openfuture.api.component.scaffold.compiler

import io.openfuture.api.component.template.TemplateProcessor
import io.openfuture.api.config.propety.EthereumProperties
import io.openfuture.api.domain.scaffold.EthereumScaffoldPropertyDto
import io.openfuture.api.entity.scaffold.ScaffoldVersion
import io.openfuture.api.exception.CompileException
import org.apache.commons.io.IOUtils
import org.ethereum.solidity.compiler.CompilationResult
import org.ethereum.solidity.compiler.CompilationResult.ContractMetadata
import org.ethereum.solidity.compiler.SolidityCompiler
import org.ethereum.solidity.compiler.SolidityCompiler.Options.*
import java.nio.charset.Charset

abstract class EthereumScaffoldCompiler(
        private val version: ScaffoldVersion,
        private val templateProcessor: TemplateProcessor,
        private val properties: EthereumProperties
) : VersionedScaffoldCompiler {

    companion object {
        private const val SCAFFOLD_KEY = "OpenScaffold"
        private const val STRUCT_PROPERTIES = "SCAFFOLD_STRUCT_PROPERTIES"
        private const val CUSTOM_PARAMETERS = "CUSTOM_SCAFFOLD_PARAMETERS"
        private const val TRANSACTION_ARGUMENTS = "SCAFFOLD_STRUCT_TRANSACTION_ARGUMENTS"
        private const val RETURN_VARIABLES = "CUSTOM_RETURN_VARIABLES"
        private const val OPEN_TOKEN_ADDRESS = "OPEN_TOKEN_ADDRESS"
    }

    override fun getVersion(): ScaffoldVersion = version

    override fun compile(properties: List<EthereumScaffoldPropertyDto>): ContractMetadata {
        val scaffold = generateScaffold(properties)
        val compiled = SolidityCompiler.compile(scaffold, true, ABI, BIN, INTERFACE, METADATA)

        if (compiled.isFailed) {
            throw CompileException(compiled.errors)
        }

        return CompilationResult.parse(compiled.output).getContract(SCAFFOLD_KEY)
    }

    protected open fun generateScaffold(properties: List<EthereumScaffoldPropertyDto>): ByteArray {
        val parameters = mapOf(
                STRUCT_PROPERTIES to properties.joinToString(separator = ";\n\t", postfix = ";") { "${it.type!!.getValue()} ${it.name}" },
                CUSTOM_PARAMETERS to properties.joinToString(separator = ", ") { "${it.type!!.getValue()} ${it.name}" },
                TRANSACTION_ARGUMENTS to properties.joinToString(separator = ",\n\t") { "${it.name}: ${it.name}" },
                RETURN_VARIABLES to properties.joinToString(separator = ",") { "${it.name}" },
                OPEN_TOKEN_ADDRESS to this.properties.openTokenAddress!!
        )

        val resource = javaClass.classLoader.getResource("templates/ethereum/scaffold_${version.name.toLowerCase()}.ftl")
        val scaffoldContent = IOUtils.toString(resource, Charset.defaultCharset())
        val preparedTemplate = templateProcessor.getContent(scaffoldContent, parameters)

        return preparedTemplate.toByteArray()
    }

}