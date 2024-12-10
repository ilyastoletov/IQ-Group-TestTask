package com.ilyastoletov.domain.repository

import com.ilyastoletov.domain.model.Vacancy

interface FavouriteRepository {
    suspend fun getAllFavouriteVacancies(): Result<List<Vacancy>>
    suspend fun clearFavouritesList(): Result<Unit>
}