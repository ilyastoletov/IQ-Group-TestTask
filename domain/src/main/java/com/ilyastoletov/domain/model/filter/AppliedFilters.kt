package com.ilyastoletov.domain.model.filter

data class AppliedFilters(
    val map: FilterMap = emptyMap(),
    val salary: Int? = null
)
