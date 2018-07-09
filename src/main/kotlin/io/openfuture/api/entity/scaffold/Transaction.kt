package io.openfuture.api.entity.scaffold

import io.openfuture.api.entity.base.BaseModel
import org.web3j.protocol.core.methods.response.Log
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "transactions")
class Transaction(

        @ManyToOne
        @JoinColumn(name = "scaffold_id", nullable = false)
        val scaffold: Scaffold,

        @Column(name = "data", nullable = false)
        val data: String,

        @Column(name = "date", nullable = false)
        val date: Date = Date()

) : BaseModel() {

    companion object {
        fun of(scaffold: Scaffold, log: Log): Transaction = Transaction(scaffold, log.data)
    }

}