package io.openfuture.api.service

import io.openfuture.api.component.state.StateApi
import io.openfuture.api.domain.scaffold.SaveOpenScaffoldRequest
import io.openfuture.api.entity.auth.User
import io.openfuture.api.entity.scaffold.OpenScaffold
import io.openfuture.api.repository.OpenScaffoldRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class DefaultOpenScaffoldService(
        private val stateApi: StateApi,
        private val openKeyService: OpenKeyService,
        private val openScaffoldRepository: OpenScaffoldRepository
) : OpenScaffoldService {

    @Transactional
    override fun save(request: SaveOpenScaffoldRequest): OpenScaffold {
        val openKey = openKeyService.get(request.openKey!!)
        request.webHook?.let { stateApi.saveOpenScaffold(it, request.developerAddress) }

        return openScaffoldRepository.save(OpenScaffold.of(request, openKey))
    }

    @Transactional(readOnly = true)
    override fun getAll(user: User, pageRequest: Pageable): Page<OpenScaffold> =
            openScaffoldRepository.findAllByOpenKeyUserOrderByIdDesc(user, pageRequest)
}