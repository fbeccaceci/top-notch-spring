package com.iplastudio.boilerplate.features.security.jwt

import com.iplastudio.boilerplate.exceptions.EntityNotFoundException
import com.iplastudio.boilerplate.features.security.jwt.exceptions.InvalidRefreshTokenException
import org.springframework.stereotype.Service
import java.util.*

@Service
class RefreshTokenService(
    private val refreshTokenRepository: RefreshTokenRepository
) {

    fun getForUser(userId: UUID): RefreshToken {
        var refreshToken = refreshTokenRepository.findByUserIdAndStatus(userId, RefreshToken.Status.ENABLED)

        if (refreshToken == null) {
            refreshToken = RefreshToken(userId = userId, token = UUID.randomUUID().toString())
            refreshToken = refreshTokenRepository.save(refreshToken)
        }

        return refreshToken
    }

    fun verify(refreshToken: String): RefreshToken {
        val refreshTokenEntity = refreshTokenRepository.findByToken(refreshToken)
            ?: throw EntityNotFoundException(RefreshToken::class)

        val isValid = refreshTokenEntity.status == RefreshToken.Status.ENABLED

        if(!isValid) {
            throw InvalidRefreshTokenException()
        }

        return refreshTokenEntity
    }
}