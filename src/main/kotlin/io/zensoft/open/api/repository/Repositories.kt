package io.zensoft.open.api.repository

import io.zensoft.open.api.model.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository: JpaRepository<User, String>