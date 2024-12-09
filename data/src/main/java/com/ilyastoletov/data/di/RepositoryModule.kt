package com.ilyastoletov.data.di

import com.ilyastoletov.data.network.api.VacancyApi
import com.ilyastoletov.data.repository.VacancyRepositoryImpl
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
    fun provideVacancyRepository(vacancyApi: VacancyApi): VacancyRepository {
        return VacancyRepositoryImpl(vacancyApi)
    }

}