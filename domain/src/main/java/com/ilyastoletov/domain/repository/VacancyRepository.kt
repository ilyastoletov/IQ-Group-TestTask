package com.ilyastoletov.domain.repository

import com.ilyastoletov.domain.model.Paged
import com.ilyastoletov.domain.model.Sorting
import com.ilyastoletov.domain.model.Vacancy
import com.ilyastoletov.domain.model.filter.Filter
import com.ilyastoletov.domain.model.filter.FilterMap

interface VacancyRepository {

    suspend fun getFilters(): Result<Filter>

    suspend fun getVacanciesPaged(
        page: Int,
        searchQuery: String,
        filters: FilterMap,
        sorting: Sorting
    ): Paged<Vacancy>

}