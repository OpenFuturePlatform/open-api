package io.openfuture.api.model.scaffold

import io.openfuture.api.model.base.BaseModel
import org.web3j.protocol.core.methods.response.Log
import javax.persistence.*

/**
 * @author Kadach Alexey
 */
@Entity
@Table(name = "transactions")
class Transaction(

        @ManyToOne
        @JoinColumn(name = "scaffold_id", nullable = false)
        val scaffold: Scaffold,

        @Column(name = "data", nullable = false)
        val data: String,

        @Column(name = "type", nullable = false)
        val type: String

): BaseModel() {

        constructor(scaffold: Scaffold, log: Log): this(scaffold, log.data, log.type)

}