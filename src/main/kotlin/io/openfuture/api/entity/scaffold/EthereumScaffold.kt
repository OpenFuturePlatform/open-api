package io.openfuture.api.entity.scaffold

import io.openfuture.api.domain.scaffold.SaveEthereumScaffoldRequest
import io.openfuture.api.entity.auth.OpenKey
import io.openfuture.api.util.DictionaryUtils
import org.apache.commons.lang3.StringUtils.EMPTY
import javax.persistence.Column
import javax.persistence.DiscriminatorValue
import javax.persistence.Entity
import javax.persistence.OneToMany

@Entity
@DiscriminatorValue("ethereum")
class EthereumScaffold(

        @Column(name = "address", unique = true)
        val address: String,

        @Column(name = "abi")
        val abi: String,

        @Column(name = "fiat_amount")
        val fiatAmount: String,

        @Column(name = "currency_id")
        private val currencyId: Int,

        @Column(name = "conversion_amount")
        val conversionAmount: String,

        @OneToMany(mappedBy = "ethereumScaffold")
        val property: MutableList<EthereumScaffoldProperty> = mutableListOf(),

        @Column(name = "version_id", nullable = false)
        private val versionId: Int,

        openKey: OpenKey,

        developerAddress: String,

        description: String,

        webHook: String? = null

) : BaseScaffold(openKey, developerAddress, description, webHook) {

    fun getVersion() = DictionaryUtils.valueOf(ScaffoldVersion::class.java, versionId)

    fun getCurrency() = DictionaryUtils.valueOf(Currency::class.java, currencyId)

    companion object {

        fun of(request: SaveEthereumScaffoldRequest, openKey: OpenKey): EthereumScaffold = EthereumScaffold(
                request.address!!,
                request.abi!!,
                request.fiatAmount!!,
                request.currency!!.getId(),
                request.conversionAmount!!,
                mutableListOf(),
                request.version.getId(),
                openKey,
                request.developerAddress!!,
                request.description!!,
                if (EMPTY == request.webHook?.trim()) null else request.webHook
        )
    }

}