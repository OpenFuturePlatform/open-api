package io.openfuture.api.entity.scaffold

import io.openfuture.api.domain.scaffold.DeployScaffoldRequest
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
        val enabled: Boolean = false

) : BaseModel() {

    fun getCurrency() = DictionaryUtils.valueOf(Currency::class.java, currencyId)

    companion object {
        fun of(address: String, openKey: OpenKey, abi: String, scaffold: DeployScaffoldRequest): Scaffold = Scaffold(
                address,
                openKey,
                abi,
                scaffold.developerAddress!!,
                scaffold.description!!,
                scaffold.fiatAmount!!,
                scaffold.currency!!.getId(),
                scaffold.conversionAmount!!
        )
    }

}