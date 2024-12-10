package com.ilyastoletov.data.repository

import com.ilyastoletov.data.storage.dao.VacancyDao
import com.ilyastoletov.data.storage.mapper.toVacancy
import com.ilyastoletov.domain.model.Vacancy
import com.ilyastoletov.domain.repository.FavouriteRepository

internal class FavouriteRepositoryImpl(
    private val vacancyDao: VacancyDao
) : FavouriteRepository {

    override suspend fun getAllFavouriteVacancies(): Result<List<Vacancy>> {
        return runCatching {
            vacancyDao.getAllVacancies().map { it.toVacancy() }
        }
    }

    override suspend fun clearFavouritesList(): Result<Unit> {
        return runCatching { vacancyDao.clear() }
    }

}