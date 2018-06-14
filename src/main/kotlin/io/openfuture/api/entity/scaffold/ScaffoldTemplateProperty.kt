package io.openfuture.api.entity.scaffold

import io.openfuture.api.domain.scaffold.ScaffoldTemplatePropertyDto
import io.openfuture.api.entity.base.BaseModel
import io.openfuture.api.util.DictionaryUtils
import javax.persistence.*

/**
 * @author Kadach Alexey
 */
@Entity
@Table(name = "scaffold_template_properties")
class ScaffoldTemplateProperty(

        @ManyToOne
        @JoinColumn(name = "scaffold_template_id", nullable = false)
        val scaffold: ScaffoldTemplate,

        @Column(name = "name")
        var name: String?,

        @Column(name = "type_id")
        private var typeId: Int?,

        @Column(name = "default_value")
        var defaultValue: String?

) : BaseModel() {

    fun getType() = typeId?.let { DictionaryUtils.valueOf(PropertyType::class.java, it) }

    companion object {
        fun of(scaffold: ScaffoldTemplate, property: ScaffoldTemplatePropertyDto): ScaffoldTemplateProperty = ScaffoldTemplateProperty(
                scaffold,
                property.name,
                property.type?.getId(),
                property.defaultValue
        )
    }

}