package io.openfuture.api.entity.scaffold

import io.openfuture.api.entity.base.BaseModel
import javax.persistence.*

@Entity
@Table(name = "ethereum_share_holders")
class EthereumShareHolder(

        @ManyToOne
        @JoinColumn(name = "summary_id", nullable = false)
        val summary: EthereumScaffoldSummary,

        @Column(name = "address", nullable = false)
        val address: String,

        @Column(name = "percent", nullable = false)
        val percent: Int

): BaseModel()