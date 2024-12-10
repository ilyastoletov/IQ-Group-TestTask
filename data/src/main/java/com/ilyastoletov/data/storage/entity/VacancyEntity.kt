package com.ilyastoletov.data.storage.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favourite_vacancies")
data class VacancyEntity(
    @PrimaryKey val id: String,
    val name: String,
    val salary: String,
    val company: String,
    val location: String
)
