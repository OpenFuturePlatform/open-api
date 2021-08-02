package io.openfuture.api.component.scaffold.compiler

import io.openfuture.api.component.solidity.CompilationResult
import io.openfuture.api.component.solidity.SolidityCompiler
import io.openfuture.api.component.template.TemplateProcessor
import io.openfuture.api.config.propety.EthereumProperties
import io.openfuture.api.domain.scaffold.EthereumScaffoldPropertyDto
import io.openfuture.api.entity.scaffold.ScaffoldVersion
import io.openfuture.api.exception.CompileException
import org.apache.commons.io.IOUtils
import java.nio.charset.Charset

abstract class EthereumScaffoldCompiler(
        private val version: ScaffoldVersion,
        private val templateProcessor: TemplateProcessor,
        private val properties: EthereumProperties,
        private val solidityCompiler: SolidityCompiler,
        private val compilationResult: CompilationResult
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

    override fun compile(properties: List<EthereumScaffoldPropertyDto>): CompilationResult.ContractMetadata {
        val scaffold = generateScaffold(properties)

        val compiled = solidityCompiler.compile(scaffold)

        if (compiled.isFailed) {
            throw CompileException(compiled.errors)
        }

        return compilationResult.parse(compiled.output).getContract(SCAFFOLD_KEY)
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