package com.ilyastoletov.data.repository

import com.ilyastoletov.data.network.api.VacancyApi
import com.ilyastoletov.data.network.constant.NetworkConstants
import com.ilyastoletov.data.network.extension.awaitResult
import com.ilyastoletov.data.network.mapper.toFilter
import com.ilyastoletov.data.network.mapper.toPagedVacancies
import com.ilyastoletov.data.network.mapper.toRussianRegionsFilterValues
import com.ilyastoletov.domain.model.Paged
import com.ilyastoletov.domain.model.Sorting
import com.ilyastoletov.domain.model.Vacancy
import com.ilyastoletov.domain.model.filter.AppliedFilters
import com.ilyastoletov.domain.model.filter.Filter
import com.ilyastoletov.domain.model.filter.FilterMap
import com.ilyastoletov.domain.repository.VacancyRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

internal class VacancyRepositoryImpl(
    private val api: VacancyApi
) : VacancyRepository {

    override suspend fun getFilters(): Result<Filter> {
        val areas = api.getAreas().awaitResult()
            .mapCatching { it.toRussianRegionsFilterValues() }
            .getOrDefault(emptyList())

        return api.getDictionaries().awaitResult().mapCatching { it.toFilter(areas) }
    }

    override suspend fun getVacanciesPaged(
        page: Int,
        searchQuery: String,
        filters: AppliedFilters,
        sorting: Sorting
    ): Paged<Vacancy> = withContext(Dispatchers.IO) {
        val requestUrl = buildVacanciesRequestURL(page, searchQuery, filters, sorting)
        val vacanciesPortion = api.getVacancies(requestUrl).awaitResult().getOrThrow()
        vacanciesPortion.toPagedVacancies()
    }

    private fun buildVacanciesRequestURL(
        page: Int,
        searchQuery: String,
        filters: AppliedFilters,
        sorting: Sorting
    ): String {
        val url = StringBuilder(NetworkConstants.BASE_URL)
        url.apply {
            append("vacancies?")
            append("page=$page")
            append("&per_page=20")
            if (searchQuery.isNotEmpty()) {
                append("&text=$searchQuery")
            }
            if (sorting == Sorting.DATE) {
                append("&order_by=publication_time")
            }
            filters.salary?.let {
                append("&salary=$it&currency=RUR&only_with_salary=true")
            }
            appendWithFilterValues(filters.map)
        }
        return url.toString()
    }

    private fun StringBuilder.appendWithFilterValues(filters: FilterMap): StringBuilder {
        filters.keys.forEach { key ->
            val values = filters[key]
            val requestKey = key.name.lowercase()
            if (!values.isNullOrEmpty()) {
                values.forEach { value ->
                    this.append("&$requestKey=${value.id}")
                }
            }
        }
        return this
    }

}