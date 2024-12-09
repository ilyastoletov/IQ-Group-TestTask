package com.ilyastoletov.data.network.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class AreaResponse(
    val id: String = "",
    val name: String = "",
    val areas: List<AreaResponse> = emptyList()
)
