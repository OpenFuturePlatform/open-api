package io.openfuture.api.entity.auth

import io.openfuture.api.domain.scaffold.GenerateOpenKeyRequest
import io.openfuture.api.entity.base.BaseModel
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

        @Column(name = "expired_date")
        val expiredDate: Date? = null,

        @Column(name = "value", nullable = false, unique = true)
        val value: String = "op_pk_${UUID.randomUUID()}",

        @Column(name = "enabled", nullable = false)
        var enabled: Boolean = true

) : BaseModel() {

    companion object {
        fun of(request: GenerateOpenKeyRequest, user: User): OpenKey = OpenKey(
                user,
                request.expireDate
        )
    }

}