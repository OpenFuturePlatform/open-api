package io.openfuture.api.model.scaffold

import io.openfuture.api.model.auth.OpenKey
import io.openfuture.api.model.auth.User
import io.openfuture.api.model.base.BaseModel
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
        @JoinColumn(name = "user_id", nullable = false)
        val user: User,

        @ManyToOne
        @JoinColumn(name = "open_key_id", nullable = false)
        val openKey: OpenKey,

        @Column(name = "abi", nullable = false)
        val abi: String,

        @Column(name = "enabled", nullable = false)
        val enabled: Boolean = false

): BaseModel()