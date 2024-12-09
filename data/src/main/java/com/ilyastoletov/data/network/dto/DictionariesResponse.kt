package com.ilyastoletov.data.network.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class DictionariesResponse(
    val experience: List<DictionaryItemResponse> = emptyList(),
    val employment: List<DictionaryItemResponse> = emptyList(),
    val schedule: List<DictionaryItemResponse> = emptyList(),
)
