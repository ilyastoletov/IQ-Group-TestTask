package com.ilyastoletov.data.network.mapper

import com.ilyastoletov.data.network.dto.vacancy.PagedVacanciesResponse
import com.ilyastoletov.data.network.dto.vacancy.SalaryResponse
import com.ilyastoletov.data.network.dto.vacancy.VacancyResponse
import com.ilyastoletov.domain.model.Paged
import com.ilyastoletov.domain.model.Vacancy

internal fun PagedVacanciesResponse.toPagedVacancies(): Paged<Vacancy> {
    return Paged(
        page = page,
        pages = pages,
        items = items.map { it.toVacancy() }
    )
}

private fun VacancyResponse.toVacancy(): Vacancy {
    return Vacancy(
        id = id,
        title = name,
        salary = salary?.getDisplaySalary().orEmpty(),
        company = employer.name,
        location = area.name,
    )
}

private fun SalaryResponse.getDisplaySalary(): String {
    val sb = StringBuilder()
    from?.let { sb.append("от ${it.toInt()} ") }
    to?.let { sb.append("до ${it.toInt()} ") }
    sb.append(
        if (currency == "RUR") "рублей" else currency
    )
    return sb.toString()
}