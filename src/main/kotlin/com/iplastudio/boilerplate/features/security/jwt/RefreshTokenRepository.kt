package com.iplastudio.boilerplate.features.security.jwt

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface RefreshTokenRepository: JpaRepository<RefreshToken, UUID> {

    fun findByUserIdAndStatus(userId: UUID, status: RefreshToken.Status): RefreshToken?

    fun findByToken(token: String): RefreshToken?

}