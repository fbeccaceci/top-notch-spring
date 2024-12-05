package com.iplastudio.boilerplate.features.crud

import com.fasterxml.jackson.annotation.JsonProperty
import com.iplastudio.boilerplate.features.auditing.Auditable
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import java.util.*

@Entity(name = "books")
class Book(
    val name: String,
    val author: String,
    var publisher: String,
    @Id
    @GeneratedValue
    @field:JsonProperty(access = JsonProperty.Access.READ_ONLY)
    override var id: UUID? = null
) : Auditable(), CrudManagedEntity<UUID>