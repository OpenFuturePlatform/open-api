package io.openfuture.api.entity.token

import io.openfuture.api.domain.token.UserTokenRequest
import io.openfuture.api.entity.auth.User
import io.openfuture.api.entity.base.BaseModel
import javax.persistence.*

@Entity
@Table(name = "user_custom_token")
class UserCustomToken(
    @Column(name = "name", nullable = false, unique = true)
    val name: String,

    @Column(name = "symbol")
    val symbol: String,

    @Column(name = "decimal")
    val decimal: Int,

    @Column(name = "address", nullable = false, unique = true)
    val address: String,

    @ManyToOne
    @JoinColumn(name = "user_id")
    val user: User,

    @Column(name = "active")
    val active: Boolean = true,

    @Column(name = "token_type")
    val tokenType: Int

) : BaseModel() {
    companion object {

        fun of(request: UserTokenRequest, user: User): UserCustomToken {

            return UserCustomToken(
                request.name,
                request.symbol,
                request.decimal,
                request.address,
                user,
                true,
                0
            )
        }
    }
}
