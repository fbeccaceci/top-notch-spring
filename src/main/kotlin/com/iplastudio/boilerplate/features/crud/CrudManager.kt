package com.iplastudio.boilerplate.features.crud

import com.iplastudio.boilerplate.exceptions.EntityNotFoundException
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import kotlin.reflect.KClass

interface CrudManagedEntity<TEntityId> {
    var id: TEntityId?
}

fun interface CrudAccessControlValidator<TEntity : CrudManagedEntity<TEntityId>, TEntityId> {
    fun validateAccess(entity: TEntity)
}

class CrudManager<TEntity : CrudManagedEntity<TEntityId>, TEntityId>(
    private val repository: JpaRepository<TEntity, TEntityId>,
    private val entityClass: KClass<*>,
    private val accessControlValidator: CrudAccessControlValidator<TEntity, TEntityId>? = null
) {

    fun getAll(pageable: Pageable): Page<TEntity> {
        return repository.findAll(pageable)
    }

    fun getById(@PathVariable("id") id: TEntityId): TEntity {
        val entity = repository.findByIdOrNull(id)
            ?: throw EntityNotFoundException(entityClass)

        validateAccess(entity)

        return entity
    }

    fun save(@RequestBody @Valid entity: TEntity): TEntity {
        return repository.save(entity)
    }

    fun update(@PathVariable("id") id: TEntityId, @RequestBody @Valid entity: TEntity): TEntity {
        val entityToUpdate = repository.findByIdOrNull(id)
            ?: throw EntityNotFoundException(Book::class)

        validateAccess(entityToUpdate)

        entity.id = id
        return repository.save(entity)
    }

    fun delete(@PathVariable("id") id: TEntityId) {
        val entityToDelete = repository.findByIdOrNull(id) ?: return

        validateAccess(entityToDelete)

        repository.delete(entityToDelete)
    }

    private fun validateAccess(entity: TEntity) {
        accessControlValidator?.validateAccess(entity)
    }

    class Builder<TEntity : CrudManagedEntity<TEntityId>, TEntityId> (
        private val entityClass: KClass<*>
    ) {
        private var repository: JpaRepository<TEntity, TEntityId>? = null
        private var accessValidator: CrudAccessControlValidator<TEntity, TEntityId>? = null

        fun repository(repository: JpaRepository<TEntity, TEntityId>): Builder<TEntity, TEntityId> {
            this.repository = repository
            return this
        }

        fun accessValidator(accessValidator: CrudAccessControlValidator<TEntity, TEntityId>): Builder<TEntity, TEntityId> {
            this.accessValidator = accessValidator
            return this
        }

        fun build(): CrudManager<TEntity, TEntityId> {
            if (repository == null) {
               throw IllegalStateException("Repository must be set before calling build")
            }

            return CrudManager(repository!!, entityClass)
        }

    }

}