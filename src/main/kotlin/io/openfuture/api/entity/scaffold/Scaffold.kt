package io.openfuture.api.entity.scaffold

import io.openfuture.api.domain.scaffold.SaveScaffoldRequest
import io.openfuture.api.entity.auth.OpenKey
import io.openfuture.api.entity.base.BaseModel
import io.openfuture.api.util.DictionaryUtils
import org.apache.commons.lang3.StringUtils.EMPTY
import javax.persistence.*

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
        var description: String,

        @Column(name = "fiat_amount", nullable = false)
        val fiatAmount: String,

        @Column(name = "currency_id", nullable = false)
        private val currencyId: Int,

        @Column(name = "conversion_amount", nullable = false)
        val conversionAmount: String,

        @Column(name = "web_hook")
        var webHook: String? = null,

        @OneToMany(mappedBy = "scaffold")
        val property: MutableList<ScaffoldProperty> = mutableListOf()

) : BaseModel() {

    fun getCurrency() = DictionaryUtils.valueOf(Currency::class.java, currencyId)

    companion object {
        fun of(request: SaveScaffoldRequest, openKey: OpenKey): Scaffold = Scaffold(
                request.address!!,
                openKey,
                request.abi!!,
                request.developerAddress!!,
                request.description!!,
                request.fiatAmount!!,
                request.currency!!.getId(),
                request.conversionAmount!!,
                if (EMPTY == request.webHook?.trim()) null else request.webHook
        )
    }

}