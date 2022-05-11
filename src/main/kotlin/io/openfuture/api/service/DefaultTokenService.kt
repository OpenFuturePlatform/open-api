package io.openfuture.api.service

import io.openfuture.api.domain.token.UserTokenRequest
import io.openfuture.api.entity.auth.User
import io.openfuture.api.entity.token.UserCustomToken
import io.openfuture.api.repository.UserTokenRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class DefaultTokenService(
    private val userTokenRepository: UserTokenRepository
) : TokenService {

    override fun getAll(pageRequest: Pageable): Page<UserCustomToken> {
        return userTokenRepository.findAll(pageRequest)
    }

    override fun getAll(): List<UserCustomToken> {
        return userTokenRepository.findAll()
    }

    override fun save(request: UserTokenRequest, user: User): UserCustomToken {
        return userTokenRepository.save(UserCustomToken.of(request, user))
    }
}