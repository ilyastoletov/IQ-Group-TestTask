package com.ilyastoletov.data.network.dto.vacancy

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class PagedVacanciesResponse(
    val page: Int = 0,
    val pages: Int = 0,
    val items: List<VacancyResponse> = emptyList()
)