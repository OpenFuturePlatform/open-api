package io.openfuture.api.entity.scaffold

import io.openfuture.api.entity.base.BaseModel
import java.math.BigInteger
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "ethereum_scaffold_summaries")
class EthereumScaffoldSummary(

        @OneToOne
        @JoinColumn(name = "scaffold_id", nullable = false, unique = true)
        val ethereumScaffold: EthereumScaffold,

        @Column(name = "transaction_index")
        val transactionIndex: BigInteger,

        @Column(name = "token_balance", nullable = false)
        val tokenBalance: BigInteger,

        @Column(name = "enabled", nullable = false)
        val enabled: Boolean,

        @Column(name = "date", nullable = false)
        val date: Date = Date(),

        @OneToMany(mappedBy = "summary")
        val ethereumShareHolders: MutableList<EthereumShareHolder> = mutableListOf()

) : BaseModel()