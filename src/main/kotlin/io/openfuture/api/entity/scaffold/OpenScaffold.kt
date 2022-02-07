package io.openfuture.api.entity.scaffold

import io.openfuture.api.domain.scaffold.SaveOpenScaffoldRequest
import org.apache.commons.lang3.StringUtils.EMPTY
import javax.persistence.DiscriminatorValue
import javax.persistence.Entity

@Entity
@DiscriminatorValue("open")
class OpenScaffold(

    developerAddress: String,

    description: String,

    webHook: String? = null

) : BaseScaffold(developerAddress, description, webHook) {

    companion object {
        fun of(request: SaveOpenScaffoldRequest): OpenScaffold = OpenScaffold(
            request.developerAddress,
            request.description,
            if (EMPTY == request.webHook?.trim()) null else request.webHook
        )
    }

}