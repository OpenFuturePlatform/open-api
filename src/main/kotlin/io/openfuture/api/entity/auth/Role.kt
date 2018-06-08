package io.openfuture.api.entity.auth

import io.openfuture.api.entity.base.BaseModel
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table

/**
 * @author Kadach Alexey
 */
@Entity
@Table(name = "roles")
class Role(

        @Column(name = "key", nullable = false, unique = true)
        val key: String

) : BaseModel()