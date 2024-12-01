package com.iplastudio.boilerplate.features.security.jwt

import com.iplastudio.boilerplate.features.users.User
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.stereotype.Service
import java.util.*
import javax.crypto.SecretKey


@Service
class JwtService(private val jwtProperties: JwtProperties) {

    fun generateToken(user: User): String {
        return Jwts.builder()
            .subject(user.username)
            .expiration(Date(System.currentTimeMillis() + jwtProperties.expirationTime))
            .signWith(getSigningKey())
            .compact()
    }

    private fun getSigningKey(): SecretKey {
        val keyBytes = Decoders.BASE64.decode(jwtProperties.secret)
        return Keys.hmacShaKeyFor(keyBytes)
    }

    fun extractUsername(token: String): String {
        return extractClaims(token).subject
    }

    fun extractClaims(token: String): Claims {
        return Jwts.parser()
            .verifyWith(getSigningKey())
            .build()
            .parseSignedClaims(token)
            .payload
    }

}