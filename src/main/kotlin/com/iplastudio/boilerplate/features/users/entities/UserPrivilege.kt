package com.iplastudio.boilerplate.features.users.entities

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.ManyToMany
import java.util.*

@Entity(name = "privileges")
class UserPrivilege(
    var code: String,

    @Id
    @GeneratedValue
    var id: UUID? = null
) {

    @ManyToMany(mappedBy = "privileges")
    @JsonIgnore
    val privileges: MutableCollection<UserRole> = mutableSetOf()

}