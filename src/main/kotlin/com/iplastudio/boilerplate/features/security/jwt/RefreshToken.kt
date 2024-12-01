package com.iplastudio.boilerplate.features.security.jwt

import jakarta.persistence.*
import java.util.UUID

@Entity(name = "refresh_tokens")
class RefreshToken(
    var userId: UUID,
    var token: String,

    @Enumerated(EnumType.STRING)
    var status: Status = Status.ENABLED,

    @Id
    @GeneratedValue
    var id: UUID? = null
) {

    enum class Status {
        ENABLED,
        DISABLED
    }
}