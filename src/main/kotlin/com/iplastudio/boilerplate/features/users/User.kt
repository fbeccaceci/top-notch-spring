package com.iplastudio.boilerplate.features.users

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.*

@Entity(name = "users")
class User(
    var username: String,
    var email: String,

    @field:JsonIgnore
    var password: String,

    @Enumerated(EnumType.STRING)
    var status: Status = Status.DISABLED,

    var emailVerifiedAt: LocalDateTime? = null,

    var locale: Locale = Locale.ENGLISH,

    @Id
    @GeneratedValue
    @field:JsonProperty(access = JsonProperty.Access.READ_ONLY)
    var id: UUID? = null
) {

    @OneToMany(cascade = [CascadeType.REMOVE], orphanRemoval = true, mappedBy = "userId")
    private var otps: MutableSet<UserOtp> = mutableSetOf()

    enum class Status {
        WAITING_EMAIL_VERIFICATION,
        ACTIVE,
        DISABLED;
    }

}