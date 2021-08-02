package io.openfuture.api.component.solidity

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Component
import java.io.IOException
import java.nio.file.Path
import java.nio.file.Paths

@Component
class CompilationResult {
    @JsonProperty("contracts")
    var contracts: Map<String, ContractMetadata>? = null

    @JsonProperty("version")
    var version: String? = null

    @JsonIgnore
    @Throws(IOException::class)
    fun parse(rawJson: String?): CompilationResult {
        return if (rawJson == null || rawJson.isEmpty()) {
            val empty: CompilationResult = CompilationResult()
            empty.contracts = emptyMap()
            empty.version = ""
            empty
        } else {
            ObjectMapper().readValue(rawJson, CompilationResult::class.java)
        }
    }

    @JsonIgnore
    fun getContractPath(): Path? {
        return if (contracts!!.size > 1) {
            throw UnsupportedOperationException("Source contains more than 1 contact. Please specify the contract name. Available keys (" + getContractKeys() + ").")
        } else {
            val key = contracts!!.keys.iterator().next()
            Paths.get(key.substring(0, key.lastIndexOf(':')))
        }
    }

    @JsonIgnore
    fun getContract(contractName: String?): ContractMetadata {
        if (contractName == null && contracts!!.size == 1) {
            return contracts!!.values.iterator().next()
        } else if (contractName == null || contractName.isEmpty()) {
            throw java.lang.UnsupportedOperationException("Source contains more than 1 contact. Please specify the contract name. Available keys (" + getContractKeys() + ").")
        }
        for ((key, value) in contracts!!) {
            val name = key.substring(key.lastIndexOf(':') + 1)
            if (contractName == name) {
                return value
            }
        }
        throw java.lang.UnsupportedOperationException("No contract found with name '" + contractName + "'. Please specify a valid contract name. Available keys (" + getContractKeys() + ").")
    }

    @JsonIgnore
    fun getContractName(): String? {
        return if (contracts!!.size > 1) {
            throw UnsupportedOperationException("Source contains more than 1 contact. Please specify the contract name. Available keys (" + getContractKeys() + ").")
        } else {
            val key = contracts!!.keys.iterator().next()
            key.substring(key.lastIndexOf(':') + 1)
        }
    }

    @JsonIgnore
    fun getContracts(): List<ContractMetadata>? {
        return ArrayList(contracts!!.values)
    }

    @JsonIgnore
    fun getContractKeys(): List<String> {
        return ArrayList(contracts!!.keys)
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    class ContractMetadata {
        var bin: String? = null
        var abi: String? = null
        var metadata: String? = null
    }
}