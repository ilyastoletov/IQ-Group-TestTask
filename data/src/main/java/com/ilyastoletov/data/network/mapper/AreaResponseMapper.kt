package com.ilyastoletov.data.network.mapper

import com.ilyastoletov.data.network.dto.AreaResponse
import com.ilyastoletov.domain.model.filter.FilterValue

internal fun List<AreaResponse>.toRussianRegionsFilterValues(): List<FilterValue> {
    val russianAreas = this.find { it.name == "Россия" }?.areas ?: return emptyList()
    return russianAreas.map { response ->
        FilterValue(
            id = response.id,
            name = response.name
        )
    }.sortedBy { it.name }
}