package com.ilyastoletov.data.network.dto.vacancy

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class SalaryResponse(
    val from: Number? = null,
    val to: Number? = null,
    val currency: String = ""
)
