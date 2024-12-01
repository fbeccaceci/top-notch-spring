package com.iplastudio.boilerplate.features.users.entities

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

    @field:JsonIgnore
    var locale: Locale = Locale.ENGLISH,

    @Id
    @GeneratedValue
    @field:JsonProperty(access = JsonProperty.Access.READ_ONLY)
    var id: UUID? = null
) {

    @OneToMany(cascade = [CascadeType.REMOVE], orphanRemoval = true, mappedBy = "userId")
    private var otps: MutableSet<UserOtp> = mutableSetOf()

    @ManyToMany
    @JoinTable(
        name = "users_roles",
        joinColumns = [JoinColumn(name = "user_id", referencedColumnName = "id")],
        inverseJoinColumns = [JoinColumn(name = "role_id", referencedColumnName = "id")]
    )
    @JsonIgnore
    val roles: MutableCollection<UserRole> = mutableSetOf()

    @get:JsonProperty("roles")
    val rolesString: String
        get() = roles.toList().joinToString(",") { it.code }

    @get:JsonProperty("privileges")
    val privilegesString: String
        get() = roles.toList().flatMap { it.privileges.toList() }.joinToString(",") { it.code }

    enum class Status {
        WAITING_EMAIL_VERIFICATION,
        ACTIVE,
        DISABLED;
    }

}