package com.ilyastoletov.domain.model

data class Paged<T>(
    val page: Int,
    val pages: Int,
    val items: List<T>
)
