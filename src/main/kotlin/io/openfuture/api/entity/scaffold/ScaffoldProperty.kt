package io.openfuture.api.entity.scaffold

import io.openfuture.api.domain.scaffold.ScaffoldPropertyDto
import io.openfuture.api.entity.base.BaseModel
import io.openfuture.api.util.DictionaryUtils
import javax.persistence.*

@Entity
@Table(name = "scaffold_properties")
class ScaffoldProperty(

        @ManyToOne
        @JoinColumn(name = "scaffold_id", nullable = false)
        val scaffold: Scaffold,

        @Column(name = "name", nullable = false)
        val name: String,

        @Column(name = "type_id", nullable = false)
        private val typeId: Int,

        @Column(name = "default_value", nullable = false)
        val defaultValue: String

) : BaseModel() {

    fun getType() = DictionaryUtils.valueOf(PropertyType::class.java, typeId)

    companion object {
        fun of(scaffold: Scaffold, property: ScaffoldPropertyDto): ScaffoldProperty = ScaffoldProperty(
                scaffold,
                property.name!!,
                property.type!!.getId(),
                property.defaultValue!!
        )
    }

}