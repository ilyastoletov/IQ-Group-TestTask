package com.ilyastoletov.domain.usecase

import com.ilyastoletov.domain.repository.FavouriteRepository

class GetAllFavouritesUseCase(private val repository: FavouriteRepository) {
    suspend fun invoke() = repository.getAllFavouriteVacancies()
}