package com.ilyastoletov.data.network.dto.vacancy

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class VacancyResponse(
    val id: String = "",
    val name: String = "",
    val salary: SalaryResponse? = null,
    val employer: EmployerResponse = EmployerResponse(),
    val area: AreaResponse = AreaResponse()
)
