package com.ilyastoletov.data.storage.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ilyastoletov.data.storage.dao.VacancyDao
import com.ilyastoletov.data.storage.entity.VacancyEntity

@Database(entities = [VacancyEntity::class], version = 1)
abstract class VacancyDatabase : RoomDatabase() {

    abstract fun getVacanciesDao(): VacancyDao

}