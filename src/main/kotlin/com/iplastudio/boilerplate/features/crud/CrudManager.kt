package com.iplastudio.boilerplate.features.crud

import com.iplastudio.boilerplate.exceptions.EntityNotFoundException
import com.iplastudio.boilerplate.features.security.AuthenticationDetailsHolder
import jakarta.validation.Valid
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.access.AccessDeniedException
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import java.util.*
import kotlin.reflect.KClass

interface CrudManagedEntity<TEntityId> {
    var id: TEntityId?
}

fun interface CrudAccessControlValidator<TEntity : CrudManagedEntity<TEntityId>, TEntityId> {
    fun validateAccess(entity: TEntity)
}

fun interface GetAllQuery<TEntity : CrudManagedEntity<TEntityId>, TEntityId> {
    fun findAllByUserId(userId: UUID, pageable: Pageable): Page<TEntity>
}

class CrudManager<TEntity : CrudManagedEntity<TEntityId>, TEntityId>(
    private val repository: JpaRepository<TEntity, TEntityId>,
    private val entityClass: KClass<*>,
    private val accessControlValidator: CrudAccessControlValidator<TEntity, TEntityId>? = null,
    private val getAllQuery: GetAllQuery<TEntity, TEntityId>? = null
) {

    private val log = LoggerFactory.getLogger(CrudManager::class.java)

    fun getAll(pageable: Pageable): PageDto<TEntity> {
        log.debug("Performing GET ALL request for ${entityClass.simpleName}")

        if (getAllQuery != null) {
            val principal = AuthenticationDetailsHolder.details
                ?: throw AccessDeniedException("User is not authenticated")

            val result = getAllQuery.findAllByUserId(principal.userId, pageable)
            return PageDto.of(result)
        }

        val result = repository.findAll(pageable)
        return PageDto.of(result)
    }

    fun getById(@PathVariable("id") id: TEntityId): TEntity {
        log.debug("Performing GET request for {} with ID {}", entityClass.simpleName, id)

        val entity = repository.findByIdOrNull(id)
            ?: throw EntityNotFoundException(entityClass)

        validateAccess(entity)

        return entity
    }

    fun save(@RequestBody @Valid entity: TEntity): TEntity {
        log.debug("Performing SAVE request for {}", entityClass.simpleName)
        return repository.save(entity)
    }

    fun update(@PathVariable("id") id: TEntityId, @RequestBody @Valid entity: TEntity): TEntity {
        log.debug("Performing UPDATE request for {} with ID {}", entityClass.simpleName, id)

        val entityToUpdate = repository.findByIdOrNull(id)
            ?: throw EntityNotFoundException(entityClass)

        validateAccess(entityToUpdate)

        entity.id = id
        return repository.save(entity)
    }

    fun delete(@PathVariable("id") id: TEntityId) {
        log.debug("Performing DELETE request for {} with ID {}", entityClass.simpleName, id)

        val entityToDelete = repository.findByIdOrNull(id) ?: return

        validateAccess(entityToDelete)

        repository.delete(entityToDelete)
    }

    private fun validateAccess(entity: TEntity) {
        try {
            accessControlValidator?.validateAccess(entity)
        } catch (e: AccessDeniedException) {
            log.debug("Access validation failed")
            throw e
        }
    }

    class Builder<TEntity : CrudManagedEntity<TEntityId>, TEntityId>(
        private val entityClass: KClass<*>
    ) {
        private var repository: JpaRepository<TEntity, TEntityId>? = null
        private var accessValidator: CrudAccessControlValidator<TEntity, TEntityId>? = null
        private var getAllQuery: GetAllQuery<TEntity, TEntityId>? = null

        fun repository(repository: JpaRepository<TEntity, TEntityId>): Builder<TEntity, TEntityId> {
            this.repository = repository
            return this
        }

        fun accessValidator(accessValidator: CrudAccessControlValidator<TEntity, TEntityId>): Builder<TEntity, TEntityId> {
            this.accessValidator = accessValidator
            return this
        }

        fun getAllQuery(query: GetAllQuery<TEntity, TEntityId>): Builder<TEntity, TEntityId> {
            this.getAllQuery = query
            return this
        }

        fun build(): CrudManager<TEntity, TEntityId> {
            if (repository == null) {
                throw IllegalStateException("Repository must be set before calling build")
            }

            return CrudManager(repository!!, entityClass, accessValidator, getAllQuery)
        }

    }

}