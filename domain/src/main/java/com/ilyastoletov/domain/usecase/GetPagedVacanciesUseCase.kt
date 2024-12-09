package com.ilyastoletov.domain.usecase

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.ilyastoletov.domain.model.Sorting
import com.ilyastoletov.domain.model.Vacancy
import com.ilyastoletov.domain.model.filter.AppliedFilters
import com.ilyastoletov.domain.repository.VacancyRepository
import com.ilyastoletov.domain.usecase.paging.VacanciesPagingSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

class GetPagedVacanciesUseCase(private val repository: VacancyRepository) {

    fun invoke(
        searchQuery: String,
        filters: AppliedFilters,
        sorting: Sorting,
        cacheScope: CoroutineScope
    ): Flow<PagingData<Vacancy>> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            initialKey = 0,
            pagingSourceFactory = {
                VacanciesPagingSource(
                    repository = repository,
                    searchQuery = searchQuery,
                    filters = filters,
                    sorting = sorting
                )
            }
        ).flow.cachedIn(cacheScope)
    }

}