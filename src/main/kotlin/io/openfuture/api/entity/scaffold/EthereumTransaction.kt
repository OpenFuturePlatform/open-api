package io.openfuture.api.entity.scaffold

import io.openfuture.api.entity.base.BaseModel
import org.web3j.protocol.core.methods.response.Log
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "ethereum_transactions")
class EthereumTransaction(

        @Column(name = "hash", nullable = false)
        val hash: String,

        @Column(name = "index", nullable = false)
        val index: String,

        @ManyToOne
        @JoinColumn(name = "scaffold_id", nullable = false)
        val ethereumScaffold: EthereumScaffold,

        @Column(name = "data", nullable = false)
        val data: String,

        @Column(name = "date", nullable = false)
        val date: Date = Date()

) : BaseModel() {

    companion object {
        fun of(ethereumScaffold: EthereumScaffold, log: Log): EthereumTransaction = EthereumTransaction(log.transactionHash, log.logIndexRaw, ethereumScaffold, log.data)
    }

}