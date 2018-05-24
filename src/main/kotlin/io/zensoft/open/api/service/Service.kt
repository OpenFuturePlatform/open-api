package io.zensoft.open.api.service

import io.zensoft.open.api.model.Scaffold
import io.zensoft.open.api.model.User
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

/**
 * @author Kadach Alexey
 */
interface ScaffoldService {

    fun getAll(user: User, pageRequest: Pageable): Page<Scaffold>

    fun get(address: String): Scaffold

}