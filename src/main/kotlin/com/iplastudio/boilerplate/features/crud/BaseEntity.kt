package com.iplastudio.boilerplate.features.crud

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import com.iplastudio.boilerplate.features.auditing.Auditable
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.MappedSuperclass
import java.util.*

@MappedSuperclass
class BaseEntity(
    @Id
    @GeneratedValue
    @field:JsonIgnore
    override var id: UUID? = null
): Auditable(), CrudManagedEntity<UUID> {

    @JsonProperty("id", access = JsonProperty.Access.READ_ONLY)
    fun jsonId(): UUID = id
        ?: throw IllegalStateException("id should not be null at serialization time")
}