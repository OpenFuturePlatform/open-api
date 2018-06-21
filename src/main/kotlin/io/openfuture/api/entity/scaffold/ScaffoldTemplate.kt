package io.openfuture.api.entity.scaffold

import io.openfuture.api.domain.scaffold.SaveScaffoldTemplateRequest
import io.openfuture.api.entity.base.BaseModel
import io.openfuture.api.util.DictionaryUtils
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.OneToMany
import javax.persistence.Table

@Entity
@Table(name = "scaffold_templates")
class ScaffoldTemplate(

        @Column(name = "name", nullable = false, unique = true)
        val name: String,

        @Column(name = "developer_address")
        val developerAddress: String?,

        @Column(name = "description")
        val description: String?,

        @Column(name = "fiat_amount")
        val fiatAmount: String?,

        @Column(name = "currency_id")
        private val currencyId: Int?,

        @Column(name = "conversion_amount")
        val conversionAmount: String?,

        @Column(name = "web_hook")
        val webHook: String?,

        @OneToMany(mappedBy = "scaffold")
        val property: MutableList<ScaffoldTemplateProperty> = mutableListOf(),

        @Column(name = "deleted", nullable = false)
        var deleted: Boolean = false

) : BaseModel() {

    fun getCurrency() = currencyId?.let { DictionaryUtils.valueOf(Currency::class.java, currencyId) }

    companion object {
        fun of(request: SaveScaffoldTemplateRequest): ScaffoldTemplate = ScaffoldTemplate(
                request.name!!,
                request.developerAddress,
                request.description,
                request.fiatAmount,
                request.currency?.getId(),
                request.conversionAmount,
                request.webHook
        )
    }

}