package com.ilyastoletov.data.storage.di

import android.content.Context
import androidx.room.Room
import com.ilyastoletov.data.storage.dao.VacancyDao
import com.ilyastoletov.data.storage.database.VacancyDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class StorageModule {

    @Provides
    fun provideVacancyDatabase(@ApplicationContext context: Context): VacancyDatabase {
        return Room.databaseBuilder(context, VacancyDatabase::class.java, name = "main.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideVacancyDao(database: VacancyDatabase): VacancyDao = database.getVacanciesDao()

}