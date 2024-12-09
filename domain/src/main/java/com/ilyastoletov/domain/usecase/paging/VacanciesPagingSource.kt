package com.ilyastoletov.domain.usecase.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.ilyastoletov.domain.model.Sorting
import com.ilyastoletov.domain.model.Vacancy
import com.ilyastoletov.domain.model.filter.AppliedFilters
import com.ilyastoletov.domain.repository.VacancyRepository

internal class VacanciesPagingSource(
    private val repository: VacancyRepository,
    private val searchQuery: String,
    private val filters: AppliedFilters,
    private val sorting: Sorting
) : PagingSource<Int, Vacancy>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Vacancy> {
        return try {

            val page = params.key ?: 0
            val result = repository.getVacanciesPaged(page, searchQuery, filters, sorting)
            val nextPage = if (result.pages != page) page + 1 else null

            LoadResult.Page(
                data = result.items,
                prevKey = null,
                nextKey = nextPage
            )
        } catch(e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Vacancy>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

}