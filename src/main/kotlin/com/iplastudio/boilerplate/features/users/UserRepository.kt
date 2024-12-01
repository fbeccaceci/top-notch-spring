package com.iplastudio.boilerplate.features.users

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserRepository: JpaRepository<User, UUID> {
    fun existsByUsernameOrEmail(username: String, email: String): Boolean

    fun findByEmailOrUsername(email: String, username: String): User?

    fun findByUsername(username: String): User?
}