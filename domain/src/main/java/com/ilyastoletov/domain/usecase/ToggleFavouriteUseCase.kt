package com.ilyastoletov.domain.usecase

import com.ilyastoletov.domain.model.Vacancy
import com.ilyastoletov.domain.repository.VacancyRepository

class ToggleFavouriteUseCase(private val repository: VacancyRepository) {
    suspend fun invoke(model: Vacancy) = repository.toggleFavourite(model)
}