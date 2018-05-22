package io.zensoft.open.api.repository

import io.zensoft.open.api.model.User
import org.springframework.data.mongodb.repository.MongoRepository

interface UserRepository: MongoRepository<User, String>