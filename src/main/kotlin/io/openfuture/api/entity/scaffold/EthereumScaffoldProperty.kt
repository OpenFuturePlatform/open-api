package io.openfuture.api.entity.scaffold

import io.openfuture.api.domain.scaffold.EthereumScaffoldPropertyDto
import io.openfuture.api.entity.base.BaseModel
import io.openfuture.api.util.DictionaryUtils
import javax.persistence.*

@Entity
@Table(name = "ethereum_scaffold_properties")
class EthereumScaffoldProperty(

        @ManyToOne
        @JoinColumn(name = "scaffold_id", nullable = false)
        val ethereumScaffold: EthereumScaffold,

        @Column(name = "name", nullable = false)
        val name: String,

        @Column(name = "type_id", nullable = false)
        private val typeId: Int,

        @Column(name = "default_value")
        val defaultValue: String?

) : BaseModel() {

    fun getType() = DictionaryUtils.valueOf(PropertyType::class.java, typeId)

    companion object {
        fun of(ethereumScaffold: EthereumScaffold, property: EthereumScaffoldPropertyDto): EthereumScaffoldProperty = EthereumScaffoldProperty(
                ethereumScaffold,
                property.name!!,
                property.type!!.getId(),
                property.defaultValue
        )
    }

}