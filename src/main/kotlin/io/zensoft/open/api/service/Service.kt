package io.zensoft.open.api.service

import io.zensoft.open.api.model.Scaffold
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

/**
 * @author Kadach Alexey
 */
interface ScaffoldService {

    fun getAll(pageRequest: Pageable): Page<Scaffold>

}