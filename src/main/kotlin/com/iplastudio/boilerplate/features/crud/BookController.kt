package com.iplastudio.boilerplate.features.crud

import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springdoc.core.annotations.ParameterObject
import org.springdoc.core.converters.models.PageableAsQueryParam
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.data.web.SortDefault
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.util.*

@Tag(name = "BooksCrud")
@RestController
@RequestMapping("/books")
final class BookCrudController(bookRepository: BookRepository) {
    private val bookCrudManager = CrudManager.Builder<Book, UUID>(Book::class)
        .repository(bookRepository)
        .accessValidator(this::validateAccess)
        .build()

    private fun validateAccess(book: Book) {
        // Check here if the user has access to the book else throw AccessDeniedException
    }

    @GetMapping
    fun getAll(@SortDefault(sort = ["createdAt,DESC"]) @PageableDefault(size = 20) @ParameterObject pageable: Pageable): Page<Book>
    = bookCrudManager.getAll(pageable)

    @GetMapping("/{id}")
    fun getById(@PathVariable("id") id: UUID): Book = bookCrudManager.getById(id)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun save(@RequestBody @Valid book: Book): Book = bookCrudManager.save(book)

    @PutMapping("/{id}")
    fun update(@PathVariable("id") id: UUID, @RequestBody @Valid entity: Book): Book = bookCrudManager.update(id, entity)

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable("id") id: UUID) = bookCrudManager.delete(id)
}
