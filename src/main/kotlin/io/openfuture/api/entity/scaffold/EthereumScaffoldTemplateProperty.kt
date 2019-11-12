package io.openfuture.api.entity.scaffold

import io.openfuture.api.domain.scaffold.EthereumScaffoldTemplatePropertyDto
import io.openfuture.api.entity.base.BaseModel
import io.openfuture.api.util.DictionaryUtils
import javax.persistence.*

@Entity
@Table(name = "ethereum_scaffold_template_properties")
class EthereumScaffoldTemplateProperty(

        @ManyToOne
        @JoinColumn(name = "scaffold_template_id", nullable = false)
        val ethereumScaffoldTemplate: EthereumScaffoldTemplate,

        @Column(name = "name")
        val name: String?,

        @Column(name = "type_id")
        private val typeId: Int?,

        @Column(name = "default_value")
        val defaultValue: String?

) : BaseModel() {

    fun getType() = typeId?.let { DictionaryUtils.valueOf(PropertyType::class.java, it) }

    companion object {
        fun of(ethereumScaffoldTemplate: EthereumScaffoldTemplate, property: EthereumScaffoldTemplatePropertyDto): EthereumScaffoldTemplateProperty = EthereumScaffoldTemplateProperty(
                ethereumScaffoldTemplate,
                property.name,
                property.type?.getId(),
                property.defaultValue
        )
    }

}