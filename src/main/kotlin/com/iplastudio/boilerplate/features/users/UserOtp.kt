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
    var status: Status = Status.VALID,

    @Id
    @GeneratedValue
    var id: UUID? = null
) {
    enum class Status {
        VALID, // The otp can be used
        REPLACED, // The token has been replaced by a new one
        USED // The otp has been used
    }

    enum class Type {
        ACCOUNT_ACTIVATION,
    }
}