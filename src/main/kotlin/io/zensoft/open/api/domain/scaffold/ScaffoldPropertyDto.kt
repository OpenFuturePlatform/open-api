package io.zensoft.open.api.domain.scaffold

import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

/**
 * @author Kadach Alexey
 */
data class ScaffoldPropertyDto(
        @field:NotBlank var name: String? = null,
        @field:NotNull var type: PropertyType? = null,
        @field:NotBlank var defaultValue: String? = null
)

enum class PropertyType(private val value: String) {

    STRING("string"),
    NUMBER("number"),
    BOOLEAN("boolean");

    fun getValue(): String = value

}