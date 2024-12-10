package com.ilyastoletov.domain.repository

import com.ilyastoletov.domain.model.Paged
import com.ilyastoletov.domain.model.Sorting
import com.ilyastoletov.domain.model.Vacancy
import com.ilyastoletov.domain.model.filter.AppliedFilters
import com.ilyastoletov.domain.model.filter.Filter

interface VacancyRepository {

    suspend fun getFilters(): Result<Filter>

    suspend fun getVacanciesPaged(
        page: Int,
        searchQuery: String,
        filters: AppliedFilters,
        sorting: Sorting
    ): Paged<Vacancy>

    suspend fun toggleFavourite(model: Vacancy): Result<Unit>

}