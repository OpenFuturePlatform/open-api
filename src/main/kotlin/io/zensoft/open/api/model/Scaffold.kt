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

        @Column(name = "abi", nullable = false)
        val abi: String

): BaseModel()