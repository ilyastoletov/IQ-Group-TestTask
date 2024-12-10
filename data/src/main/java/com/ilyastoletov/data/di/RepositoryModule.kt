package com.ilyastoletov.data.di

import com.ilyastoletov.data.network.api.VacancyApi
import com.ilyastoletov.data.repository.FavouriteRepositoryImpl
import com.ilyastoletov.data.repository.VacancyRepositoryImpl
import com.ilyastoletov.data.storage.dao.VacancyDao
import com.ilyastoletov.domain.repository.FavouriteRepository
import com.ilyastoletov.domain.repository.VacancyRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Provides
    @Singleton
    fun provideVacancyRepository(vacancyApi: VacancyApi, dao: VacancyDao): VacancyRepository {
        return VacancyRepositoryImpl(vacancyApi, dao)
    }

    @Provides
    @Singleton
    fun provideFavouriteRepository(dao: VacancyDao): FavouriteRepository {
        return FavouriteRepositoryImpl(dao)
    }

}