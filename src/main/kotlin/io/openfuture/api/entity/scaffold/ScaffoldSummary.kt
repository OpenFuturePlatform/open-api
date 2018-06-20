package io.openfuture.api.entity.scaffold

import io.openfuture.api.entity.base.BaseModel
import java.math.BigInteger
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "scaffold_summaries")
class ScaffoldSummary(

        @ManyToOne
        @JoinColumn(name = "scaffold_id", nullable = false)
        val scaffold: Scaffold,

        @Column(name = "transaction_index")
        val transactionIndex: BigInteger,

        @Column(name = "token_balance", nullable = false)
        val tokenBalance: BigInteger,

        @Column(name = "enabled", nullable = false)
        val enabled: Boolean,

        @Column(name = "date", nullable = false)
        val date: Date = Date()

) : BaseModel()