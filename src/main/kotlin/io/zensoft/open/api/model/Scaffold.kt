package io.zensoft.open.api.model

import io.zensoft.open.api.model.base.BaseModel
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
        val abi: String

): BaseModel()