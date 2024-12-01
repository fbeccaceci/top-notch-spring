package com.iplastudio.boilerplate.features.users.entities

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import java.util.*


@Entity(name = "roles")
class UserRole(
    var name: String,

    var code: String,

    @Id
    @GeneratedValue
    var id: UUID? = null
) {

    @ManyToMany(mappedBy = "roles")
    @JsonIgnore
    val users: MutableCollection<User> = mutableSetOf()

    @ManyToMany
    @JoinTable(
        name = "roles_privileges",
        joinColumns = [JoinColumn(name = "role_id", referencedColumnName = "id")],
        inverseJoinColumns = [JoinColumn(name = "privilege_id", referencedColumnName = "id")]
    )
    @JsonIgnore
    val privileges: MutableCollection<UserPrivilege> = mutableSetOf()
}