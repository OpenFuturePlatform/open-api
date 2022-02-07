package io.openfuture.api.entity.auth

import io.openfuture.api.entity.base.BaseModel
import javax.persistence.*
import javax.persistence.FetchType.EAGER

@Entity
@Table(name = "users")
class User(

        @Column(name = "google_id", nullable = false, unique = true)
        val googleId: String,

        @Column(name = "credits", nullable = false)
        val credits: Int = 0,

        @ManyToMany(fetch = EAGER)
        @JoinTable(
                name = "users2roles",
                joinColumns = [(JoinColumn(name = "user_id", nullable = false))],
                inverseJoinColumns = [(JoinColumn(name = "role_id", nullable = false))]
        )
        val roles: Set<Role> = setOf()

) : BaseModel()