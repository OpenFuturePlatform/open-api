package io.openfuture.api.entity.auth

import io.openfuture.api.entity.base.BaseModel
import javax.persistence.*
import javax.persistence.FetchType.EAGER

/**
 * @author Kadach Alexey
 */
@Entity
@Table(name = "users")
class User(

        @Column(name = "google_id", nullable = false, unique = true)
        val googleId: String,

        @Column(name = "credits", nullable = false)
        val credits: Int = 0,

        @OneToMany(mappedBy = "user", fetch = EAGER)
        val openKeys: MutableSet<OpenKey> = mutableSetOf(),

        @ManyToMany(fetch = EAGER)
        @JoinTable(
                name = "users2roles",
                joinColumns = [(JoinColumn(name = "user_id", nullable = false))],
                inverseJoinColumns = [(JoinColumn(name = "role_id", nullable = false))]
        )
        val roles: Set<Role> = setOf()

) : BaseModel()