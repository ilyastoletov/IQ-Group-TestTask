package com.ilyastoletov.domain.util

import com.ilyastoletov.domain.model.Vacancy
import com.ilyastoletov.domain.model.filter.FilterKey
import com.ilyastoletov.domain.model.filter.FilterValue

object Mock {

    val testVacanciesList = listOf(
        Vacancy(
            title = "Разработчик ПО",
            salary = "от 125 000 р",
            company = "ООО Яндекс",
            location = "Россия, Москва"
        ),
        Vacancy(
            title = "Тестировщик мобильных приложений",
            salary = "от 95 000 до 160 000 р",
            company = "ПАО МТС",
            location = "Россия, Владивосток"
        ),
        Vacancy(
            title = "Вакуум-сварщик",
            salary = "от 560 000 р",
            company = "Лунное Агентсво",
            location = "Луна, Море Безметежности"
        ),
        Vacancy(
            title = "Ассистент стоматолога",
            salary = "от 72 000 р",
            company = "Диамант",
            location = "Россия, Тольятти"
        ),
        Vacancy(
            title = "Электрогазосварщик",
            salary = "",
            company = "ТНПС",
            location = "Россия, Тольятти"
        ),
        Vacancy(
            title = "Уборщик (ца)",
            salary = "от 35 000 р",
            company = "Поликлиника номер 3",
            location = "Россия, Самара"
        ),
    )

    val testFilters: Map<FilterKey, List<FilterValue>> = mapOf(
        FilterKey("", "Опыт работы") to listOf(
            FilterValue("", "Без опыта", ),
            FilterValue("", "от 1 года до 3 лет"),
            FilterValue("", "от 3 до 6 лет"),
            FilterValue("", "более 6 лет", ),
        ),
        FilterKey("", "Тип занятости") to listOf(
            FilterValue("", "Полная занятость", ),
            FilterValue("", "Частичная занятость", ),
            FilterValue("", "Подработка"),
            FilterValue("", "Безработка"),
        ),
        FilterKey("", "График работы") to listOf(
            FilterValue("", "Пятидневка", ),
            FilterValue("", "Шестидневка"),
            FilterValue("", "Семидневка"),
            FilterValue("", "Нуледневка"),
        ),
    )

}