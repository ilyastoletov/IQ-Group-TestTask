package com.ilyastoletov.domain.usecase

import com.ilyastoletov.domain.repository.VacancyRepository

class LoadFiltersUseCase(private val repository: VacancyRepository) {
    suspend fun invoke() = repository.getFilters()
}