package com.ilyastoletov.data.network.dto.vacancy

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class AreaResponse(val name: String = "")
