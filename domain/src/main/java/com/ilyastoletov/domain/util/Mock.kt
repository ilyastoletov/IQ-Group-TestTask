package com.ilyastoletov.domain.util

import com.ilyastoletov.domain.model.filter.Filter
import com.ilyastoletov.domain.model.filter.FilterValue

object Mock {

    val testFilter = Filter(
        experience = listOf(
            FilterValue("", "Без опыта", ),
            FilterValue("", "от 1 года до 3 лет"),
            FilterValue("", "от 3 до 6 лет"),
            FilterValue("", "более 6 лет", ),
        ),
        employment = listOf(
            FilterValue("", "Полная занятость", ),
            FilterValue("", "Частичная занятость", ),
            FilterValue("", "Подработка"),
            FilterValue("", "Безработка"),
        ),
        schedule = listOf(
            FilterValue("", "Полная занятость", ),
            FilterValue("", "Частичная занятость", ),
            FilterValue("", "Подработка"),
            FilterValue("", "Безработка"),
        ),
        area = listOf()
    )

}