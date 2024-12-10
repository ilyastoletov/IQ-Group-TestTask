package com.ilyastoletov.data.repository

import com.ilyastoletov.data.network.api.VacancyApi
import com.ilyastoletov.data.network.constant.NetworkConstants
import com.ilyastoletov.data.network.extension.awaitResult
import com.ilyastoletov.data.network.mapper.toFilter
import com.ilyastoletov.data.network.mapper.toPagedVacancies
import com.ilyastoletov.data.network.mapper.toRussianRegionsFilterValues
import com.ilyastoletov.data.storage.dao.VacancyDao
import com.ilyastoletov.data.storage.mapper.toEntity
import com.ilyastoletov.data.storage.mapper.toVacancy
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
    private val api: VacancyApi,
    private val dao: VacancyDao
) : VacancyRepository {

    override suspend fun getFilters(): Result<Filter> {
        val areas = api.getAreas().awaitResult()
            .mapCatching { it.toRussianRegionsFilterValues() }
            .getOrDefault(emptyList())

        return api.getDictionaries().awaitResult().mapCatching { it.toFilter(areas) }
    }

    override suspend fun toggleFavourite(model: Vacancy): Result<Unit> = runCatching {
        val allFavourites = dao.getAllVacancies()
        val itemInList = allFavourites.any { it.id == model.id }

        if (itemInList) {
            dao.deleteVacancy(model.id)
        } else {
            dao.insertVacancyEntity(model.toEntity())
        }
    }

    override suspend fun getVacanciesPaged(
        page: Int,
        searchQuery: String,
        filters: AppliedFilters,
        sorting: Sorting
    ): Paged<Vacancy> = withContext(Dispatchers.IO) {
        val requestUrl = buildVacanciesRequestURL(page, searchQuery, filters, sorting)
        val vacanciesPortion = api.getVacancies(requestUrl).awaitResult().getOrThrow()
        vacanciesPortion.toPagedVacancies().markFavourite()
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

    private suspend fun Paged<Vacancy>.markFavourite(): Paged<Vacancy> {
        val allFavourites = dao.getAllVacancies().map { it.toVacancy() }
        val mappedItems = this.items.map { it.copy(isFavourite = it in allFavourites) }
        return copy(items = mappedItems)
    }

}