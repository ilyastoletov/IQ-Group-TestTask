package com.ilyastoletov.iqtest.di

import com.ilyastoletov.domain.repository.FavouriteRepository
import com.ilyastoletov.domain.repository.VacancyRepository
import com.ilyastoletov.domain.usecase.ClearFavouritesUseCase
import com.ilyastoletov.domain.usecase.GetAllFavouritesUseCase
import com.ilyastoletov.domain.usecase.GetPagedVacanciesUseCase
import com.ilyastoletov.domain.usecase.LoadFiltersUseCase
import com.ilyastoletov.domain.usecase.ToggleFavouriteUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class DomainModule {

    @Provides
    fun provideLoadFiltersUseCase(repository: VacancyRepository): LoadFiltersUseCase {
        return LoadFiltersUseCase(repository)
    }

    @Provides
    fun provideGetPagedVacanciesUseCase(repository: VacancyRepository): GetPagedVacanciesUseCase {
        return GetPagedVacanciesUseCase(repository)
    }

    @Provides
    fun provideToggleFavouriteUseCase(repository: VacancyRepository): ToggleFavouriteUseCase {
        return ToggleFavouriteUseCase(repository)
    }

    @Provides
    fun provideGetAllFavouritesUseCase(repository: FavouriteRepository): GetAllFavouritesUseCase {
        return GetAllFavouritesUseCase(repository)
    }

    @Provides
    fun provideClearFavouritesUseCase(repository: FavouriteRepository): ClearFavouritesUseCase {
        return ClearFavouritesUseCase(repository)
    }

}