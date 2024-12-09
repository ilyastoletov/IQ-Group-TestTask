package com.ilyastoletov.data.network.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class DictionaryItemResponse(
    val id: String = "",
    val name: String = ""
)
