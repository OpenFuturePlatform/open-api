package io.openfuture.api.component

import io.openfuture.api.domain.scaffold.ScaffoldPropertyDto
import io.openfuture.api.exception.CompileException
import org.apache.commons.io.IOUtils
import org.ethereum.solidity.compiler.CompilationResult
import org.ethereum.solidity.compiler.CompilationResult.ContractMetadata
import org.ethereum.solidity.compiler.SolidityCompiler
import org.ethereum.solidity.compiler.SolidityCompiler.Options.*
import org.springframework.stereotype.Component
import java.nio.charset.Charset

/**
 * @author Kadach Alexey
 */
@Component
class ScaffoldCompiler(
        private val templateProcessor: TemplateProcessor
) {

    companion object {
        private const val SCAFFOLD_TEMPLATE_PATH = "templates/scaffold.ftl"
        private const val SCAFFOLD_KEY = "OpenScaffold"
        private const val STRUCT_PROPERTIES = "SCAFFOLD_STRUCT_PROPERTIES"
        private const val CUSTOM_PARAMETERS = "CUSTOM_SCAFFOLD_PARAMETERS"
        private const val TRANSACTION_ARGUMENTS = "SCAFFOLD_STRUCT_TRANSACTION_ARGUMENTS"
        private const val RETURN_VARIABLES = "CUSTOM_RETURN_VARIABLES"
    }


    fun compile(properties: List<ScaffoldPropertyDto>): ContractMetadata {
        val scaffold = generateScaffold(properties)
        val compiled = SolidityCompiler.compile(scaffold, true, ABI, BIN, INTERFACE, METADATA)

        if (compiled.isFailed) {
            throw CompileException(compiled.errors)
        }

        return CompilationResult.parse(compiled.output).getContract(SCAFFOLD_KEY)
    }

    private fun generateScaffold(properties: List<ScaffoldPropertyDto>): ByteArray {
        val parameters = mapOf(
                STRUCT_PROPERTIES to properties.joinToString(separator = ";\n\t", postfix = ";") { "${it.type!!.getValue()} ${it.name}" },
                CUSTOM_PARAMETERS to properties.joinToString(separator = ", ") { "${it.type!!.getValue()} ${it.name}" },
                TRANSACTION_ARGUMENTS to properties.joinToString(separator = ",\n\t") { "${it.name}: ${it.name}" },
                RETURN_VARIABLES to properties.joinToString(separator = ",") { "${it.name}" }
        )

        val resource = javaClass.classLoader.getResource(SCAFFOLD_TEMPLATE_PATH)
        val scaffoldContent = IOUtils.toString(resource, Charset.defaultCharset())
        val preparedTemplate = templateProcessor.getContent(scaffoldContent, parameters)

        return preparedTemplate.toByteArray()
    }

}