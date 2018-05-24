package io.zensoft.open.api.service

import io.zensoft.open.api.exception.NotFoundException
import io.zensoft.open.api.model.Scaffold
import io.zensoft.open.api.model.User
import io.zensoft.open.api.repository.ScaffoldRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * @author Kadach Alexey
 */
@Service
class DefaultScaffoldService(
        private val repository: ScaffoldRepository
) : ScaffoldService {

    @Transactional(readOnly = true)
    override fun getAll(user: User, pageRequest: Pageable): Page<Scaffold> = repository.findAllByUser(user, pageRequest)

    @Transactional(readOnly = true)
    override fun get(address: String): Scaffold = repository.findByAddress(address)
            ?: throw NotFoundException("Not found scaffold with address $address")

}