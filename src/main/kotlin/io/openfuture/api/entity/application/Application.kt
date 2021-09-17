package io.openfuture.api.entity.application

import io.openfuture.api.domain.application.ApplicationRequest
import io.openfuture.api.entity.auth.User
import io.openfuture.api.entity.base.BaseModel
import io.openfuture.api.entity.scaffold.Currency
import io.openfuture.api.util.DictionaryUtils
import javax.persistence.*

@Entity
@Table(name = "user_applications")
class Application(

    @Column(name = "name", nullable = false, unique = true)
    val name: String,

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,

    @Column(name = "currency_id")
    private val currencyId: Int?,

    @Column(name = "expiration_period", nullable = true)
    var expirationPeriod: Int?,

    @Column(name = "web_hook")
    val webHook: String?,

    @Column(name = "active")
    var active: Boolean = true

):BaseModel() {

    fun getCurrency() = currencyId?.let { DictionaryUtils.valueOf(Currency::class.java, currencyId) }

    companion object {

        fun of(request: ApplicationRequest, user: User): Application = Application(
            request.name!!,
            user,
            request.currency!!.getId(),
            request.expirationPeriod!!,
            request.webHook,
            request.active
        )
    }
}