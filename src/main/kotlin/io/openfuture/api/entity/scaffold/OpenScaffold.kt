package io.openfuture.api.entity.scaffold

import io.openfuture.api.domain.scaffold.SaveOpenScaffoldRequest
import io.openfuture.api.entity.auth.OpenKey
import org.apache.commons.lang3.StringUtils.EMPTY
import javax.persistence.DiscriminatorValue
import javax.persistence.Entity

@Entity
@DiscriminatorValue("open")
class OpenScaffold(

        openKey: OpenKey,

        developerAddress: String,

        description: String,

        webHook: String? = null

) : BaseScaffold(openKey, developerAddress, description, webHook) {

    companion object {
        fun of(request: SaveOpenScaffoldRequest, openKey: OpenKey): OpenScaffold = OpenScaffold(
                openKey,
                request.developerAddress,
                request.description,
                if (EMPTY == request.webHook?.trim()) null else request.webHook
        )
    }

}