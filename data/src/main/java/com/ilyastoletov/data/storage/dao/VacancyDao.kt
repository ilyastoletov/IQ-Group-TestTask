package com.ilyastoletov.data.storage.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.ilyastoletov.data.storage.entity.VacancyEntity

@Dao
interface VacancyDao {

    @Insert(entity = VacancyEntity::class)
    suspend fun insertVacancyEntity(entity: VacancyEntity)

    @Query("SELECT * FROM favourite_vacancies")
    suspend fun getAllVacancies(): List<VacancyEntity>

    @Query("DELETE FROM favourite_vacancies WHERE id = :id")
    suspend fun deleteVacancy(id: String)

    @Query("DELETE FROM favourite_vacancies")
    suspend fun clear()

}