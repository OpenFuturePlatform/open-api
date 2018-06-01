package io.openfuture.api.entity.auth

import io.openfuture.api.entity.base.BaseModel
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.OneToMany
import javax.persistence.Table

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

        @OneToMany(mappedBy = "user")
        val openKeys: MutableSet<OpenKey> = mutableSetOf()

): BaseModel()