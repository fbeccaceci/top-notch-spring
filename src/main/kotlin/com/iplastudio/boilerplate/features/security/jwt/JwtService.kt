package com.iplastudio.boilerplate.features.security.jwt

import com.iplastudio.boilerplate.features.users.entities.User
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Service
import java.util.*
import javax.crypto.SecretKey


@Service
class JwtService(private val jwtProperties: JwtProperties) {

    fun generateToken(user: User): String {
        return Jwts.builder()
            .subject(user.username)
            .expiration(Date(System.currentTimeMillis() + jwtProperties.expirationTime))
            .claim("userId", user.id!!)
            .claim("roles", user.rolesString)
            .claim("privileges", user.privilegesString)
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

    fun extractUserId(token: String): UUID {
        return UUID.fromString(extractClaims(token)["userId"].toString())
    }

    fun extractClaims(token: String): Claims {
        return Jwts.parser()
            .verifyWith(getSigningKey())
            .build()
            .parseSignedClaims(token)
            .payload
    }

    fun extractGrantedAuthorities(token: String): Collection<GrantedAuthority> {
        val stringRoles = extractClaims(token).get("roles", String::class.java)
        val rolesArray = if (stringRoles.isNotBlank())
            stringRoles?.split(",")?.map { SimpleGrantedAuthority("ROLE_$it") } ?: emptyList()
        else emptyList()

        val stringPrivileges = extractClaims(token).get("privileges", String::class.java)
        val privilegesArray = if(stringPrivileges.isNotBlank())
            stringPrivileges?.split(",")?.map { SimpleGrantedAuthority(it) } ?: emptyList()
        else emptyList()

        return rolesArray + privilegesArray
    }

}