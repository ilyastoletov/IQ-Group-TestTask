package com.ilyastoletov.domain.model.filter

data class Filter(
    val experience: List<FilterValue> = listOf(),
    val employment: List<FilterValue> = listOf(),
    val schedule: List<FilterValue> = listOf(),
    val area: List<FilterValue> = listOf()
)