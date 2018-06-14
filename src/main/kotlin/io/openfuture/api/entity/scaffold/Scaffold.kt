package io.openfuture.api.entity.scaffold

import io.openfuture.api.domain.scaffold.SaveScaffoldRequest
import io.openfuture.api.domain.scaffold.UpdateScaffoldRequest
import io.openfuture.api.entity.auth.OpenKey
import io.openfuture.api.entity.base.BaseModel
import io.openfuture.api.util.DictionaryUtils
import javax.persistence.*

/**
 * @author Kadach Alexey
 */
@Entity
@Table(name = "scaffolds")
class Scaffold(

        @Column(name = "address", nullable = false, unique = false)
        val address: String,

        @ManyToOne
        @JoinColumn(name = "open_key_id", nullable = false)
        val openKey: OpenKey,

        @Column(name = "abi", nullable = false)
        val abi: String,

        @Column(name = "developer_address", nullable = false)
        val developerAddress: String,

        @Column(name = "description", nullable = false)
        val description: String,

        @Column(name = "fiat_amount", nullable = false)
        val fiatAmount: String,

        @Column(name = "currency_id", nullable = false)
        private val currencyId: Int,

        @Column(name = "conversion_amount", nullable = false)
        val conversionAmount: String,

        @OneToMany(mappedBy = "scaffold")
        val property: MutableList<ScaffoldProperty> = mutableListOf(),

        @Column(name = "enabled", nullable = false)
        var enabled: Boolean = false,

        @Column(name = "web_hook")
        var webHook: String? = null

) : BaseModel() {

    fun getCurrency() = DictionaryUtils.valueOf(Currency::class.java, currencyId)

    companion object {
        fun of(scaffold: SaveScaffoldRequest, openKey: OpenKey): Scaffold = Scaffold(
                scaffold.address!!,
                openKey,
                scaffold.abi!!,
                scaffold.developerAddress!!,
                scaffold.description!!,
                scaffold.fiatAmount!!,
                scaffold.currency!!.getId(),
                scaffold.conversionAmount!!
        )

        fun of(request: UpdateScaffoldRequest, scaffold: Scaffold): Scaffold = Scaffold(
                scaffold.address,
                scaffold.openKey,
                scaffold.abi,
                scaffold.developerAddress,
                request.description!!,
                scaffold.fiatAmount,
                scaffold.currencyId,
                scaffold.conversionAmount
        ).apply { id = scaffold.id }
    }

}