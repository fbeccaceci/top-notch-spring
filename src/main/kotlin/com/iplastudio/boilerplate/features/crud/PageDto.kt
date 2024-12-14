package com.iplastudio.boilerplate.features.crud

import org.springframework.data.domain.Page

class PageDto<TEntity> private constructor(
    val content: List<TEntity>,
    val page: PageInfo
) {
    companion object {
        fun <TEntity> of(page: Page<TEntity>): PageDto<TEntity> {
            val pageInfo = PageInfo(
                size = page.size,
                number = page.number,
                totalElements = page.totalElements,
                totalPages = page.totalPages
            )
            return PageDto(page.content, pageInfo)
        }
    }

    data class PageInfo(
        val size: Int,
        val number: Int,
        val totalElements: Long,
        val totalPages: Int
    )
}
