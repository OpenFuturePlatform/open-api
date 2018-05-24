package io.zensoft.open.api.service

import io.zensoft.open.api.model.Scaffold
import io.zensoft.open.api.repository.ScaffoldRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

/**
 * @author Kadach Alexey
 */
@Service
class DefaultScaffoldService(
        private val repository: ScaffoldRepository
) : ScaffoldService {

    override fun getAll(pageRequest: Pageable): Page<Scaffold> = repository.findAll(pageRequest)

}