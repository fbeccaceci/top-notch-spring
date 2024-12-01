package com.iplastudio.boilerplate.features.users

import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.*

@Entity(name = "user_otps")
class UserOtp(
    var userId: UUID,

    @Enumerated(EnumType.STRING)
    var type: Type,

    var otpCode: String,

    var sentAt: LocalDateTime = LocalDateTime.now(),

    @Enumerated(EnumType.STRING)
    var status: Status = Status.LATEST_CREATED,

    var expiresAt : LocalDateTime? = null,

    @Id
    @GeneratedValue
    var id: UUID? = null
) {
    enum class Status {
        LATEST_CREATED, // This opt is the latest created for the given user and type, it can be used if not expired
        REPLACED, // The token has been replaced by a new one
        USED // The otp has been used
    }

    enum class Type {
        ACCOUNT_ACTIVATION,
        PASSWORD_RESET,
    }
}