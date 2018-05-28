package io.openfuture.api.model.auth

import io.openfuture.api.model.base.BaseModel
import java.util.*
import javax.persistence.*

/**
 * @author Kadach Alexey
 */
@Entity
@Table(name = "open_keys")
class OpenKey(

        @ManyToOne
        @JoinColumn(name = "user_id", nullable = false)
        val user: User,

        @Column(name = "value", nullable = false, unique = true)
        val value: String = "op_pk_${UUID.randomUUID()}",

        @Column(name = "enabled", nullable = false)
        val enabled: Boolean = true,

        @Column(name = "expired_date")
        val expiredDate: Date? = null

): BaseModel()