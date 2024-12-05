package com.ilyastoletov.iqtest.presentation.vacancies.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ilyastoletov.domain.model.Vacancy
import com.ilyastoletov.iqtest.R
import com.ilyastoletov.iqtest.presentation.theme.IQGroupTestTheme
import com.ilyastoletov.iqtest.presentation.vacancies.screen.components.SearchField
import com.ilyastoletov.iqtest.presentation.vacancies.screen.components.VacancyItem

@Composable
fun VacanciesScreen() {

    Content(emptyList())

}

@Composable
private fun Content(
    vacancies: List<Vacancy>
) {
    Scaffold(
        topBar = {
            SearchField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = 16.dp),
                query = "",
                onQueryChange = {},
                onSearch = {},
                onClickFilters = {},
                onClickSorting = {}
            )
        },
        containerColor = MaterialTheme.colorScheme.onSurface
    ) { scaffoldPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = scaffoldPadding.calculateTopPadding()),
            contentPadding = PaddingValues(
                bottom = 16.dp,
                start = 16.dp,
                end = 16.dp
            ),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            items(vacancies) { item ->
                VacancyItem(
                    title = item.title,
                    salary = item.salary,
                    company = item.company,
                    location = item.location
                )
            }

        }
    }
}

@Preview(showBackground = true)
@Composable
private fun VacanciesScreenPreview() {
    IQGroupTestTheme {
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
        Surface {
            Content(
                vacancies = testVacanciesList
            )
        }
    }
}