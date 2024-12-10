package com.ilyastoletov.domain.usecase

import com.ilyastoletov.domain.repository.FavouriteRepository

class ClearFavouritesUseCase(private val repository: FavouriteRepository) {
    suspend fun invoke() = repository.clearFavouritesList()
}