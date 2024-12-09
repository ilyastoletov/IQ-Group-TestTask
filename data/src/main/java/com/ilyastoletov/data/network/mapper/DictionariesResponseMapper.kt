package com.ilyastoletov.data.network.mapper

import com.ilyastoletov.data.network.dto.DictionariesResponse
import com.ilyastoletov.data.network.dto.DictionaryItemResponse
import com.ilyastoletov.domain.model.filter.Filter
import com.ilyastoletov.domain.model.filter.FilterValue

internal fun DictionariesResponse.toFilter(areas: List<FilterValue>): Filter {
    return Filter(
        experience = experience.toFilterValues(),
        employment = employment.toFilterValues(),
        schedule = schedule.toFilterValues(),
        area = areas
    )
}

private fun List<DictionaryItemResponse>.toFilterValues(): List<FilterValue> {
    return this.map { item ->
        FilterValue(
            id = item.id,
            name = item.name
        )
    }
}