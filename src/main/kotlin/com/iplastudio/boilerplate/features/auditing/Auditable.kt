package com.iplastudio.boilerplate.features.auditing

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.persistence.Column
import jakarta.persistence.EntityListeners
import jakarta.persistence.MappedSuperclass
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
class Auditable {

    @CreatedBy
    @Column(updatable = false)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    var createdBy: String? = null

    @CreatedDate
    @Column(updatable = false)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    var createdAt: LocalDateTime? = null

    @LastModifiedBy
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    var lastUpdatedBy: String? = null

    @LastModifiedDate
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    var lastUpdatedAt: LocalDateTime? = null
}