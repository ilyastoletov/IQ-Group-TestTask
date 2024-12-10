package com.ilyastoletov.data.storage.mapper

import com.ilyastoletov.data.storage.entity.VacancyEntity
import com.ilyastoletov.domain.model.Vacancy

internal fun VacancyEntity.toVacancy(): Vacancy {
    return Vacancy(
        id = id,
        title = name,
        salary = salary,
        company = company,
        location = location,
    )
}

internal fun Vacancy.toEntity(): VacancyEntity {
    return VacancyEntity(
        id = id,
        name = title,
        salary = salary,
        company = company,
        location = location
    )
}