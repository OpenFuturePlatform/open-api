package io.openfuture.api.entity.scaffold

import io.openfuture.api.entity.auth.OpenKey
import io.openfuture.api.entity.base.BaseModel
import javax.persistence.*

@Entity
@Table(name = "scaffolds")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(
        discriminatorType = DiscriminatorType.STRING,
        name = "network",
        length = 50
)
abstract class BaseScaffold(

        @ManyToOne
        @JoinColumn(name = "open_key_id", nullable = false)
        open var openKey: OpenKey,

        @Column(name = "developer_address", nullable = false)
        open var developerAddress: String,

        @Column(name = "description", nullable = false)
        open var description: String,

        @Column(name = "web_hook")
        open var webHook: String? = null

) : BaseModel()