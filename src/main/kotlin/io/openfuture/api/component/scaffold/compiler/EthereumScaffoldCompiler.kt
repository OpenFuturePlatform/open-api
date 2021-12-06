package io.openfuture.api.component.scaffold.compiler

import com.fasterxml.jackson.databind.ObjectMapper
import io.openfuture.api.component.solidity.CompilationResult
import io.openfuture.api.component.template.TemplateProcessor
import io.openfuture.api.config.propety.EthereumProperties
import io.openfuture.api.domain.scaffold.EthereumScaffoldPropertyDto
import io.openfuture.api.entity.scaffold.ScaffoldVersion
import io.openfuture.api.exception.CompileException
import org.apache.commons.io.IOUtils
import org.web3j.sokt.SolcArguments
import org.web3j.sokt.SolidityFile
import java.nio.charset.Charset
import java.nio.file.Files
import java.nio.file.Path

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

    override fun compile(properties: List<EthereumScaffoldPropertyDto>): CompilationResult.ContractMetadata {
        val scaffold = generateScaffold(properties)

        val tempFile: Path = Files.createTempFile("smart_contract_", ".sol")
        Files.write(tempFile, scaffold);

        val compilerInstance = SolidityFile(tempFile.toAbsolutePath().toString()).getCompilerInstance()
        val res = compilerInstance.execute(
            SolcArguments.COMBINED_JSON.param { "abi,bin,interface,metadata" }
        )
        // DELETE TMP FILE AFTER COMPILE
        Files.deleteIfExists(tempFile)

        if (res.stdOut.isEmpty())
            throw CompileException(res.stdErr)

        return parse(res.stdOut).getContract(SCAFFOLD_KEY)
    }

    private fun parse(rawJson: String): CompilationResult {
        return if (rawJson.isEmpty()) {
            val empty = CompilationResult()
            empty.contracts = emptyMap()
            empty.version = ""
            empty
        } else {
            ObjectMapper().readValue(rawJson, CompilationResult::class.java)
        }
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