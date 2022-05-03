package io.openfuture.api.entity.token

import io.openfuture.api.entity.auth.User
import io.openfuture.api.entity.base.BaseModel
import javax.persistence.*

@Entity
@Table(name = "user_address")
class UserAddress(
    @Column(name = "blockchain")
    val blockchain: String,

    @Column(name = "address")
    val address: String,

    @Column(name = "webhook")
    val webhook: String,

    @ManyToOne
    @JoinColumn(name = "user_id")
    val user: User,

    @Column(name = "active")
    val active: Boolean = true

) : BaseModel()