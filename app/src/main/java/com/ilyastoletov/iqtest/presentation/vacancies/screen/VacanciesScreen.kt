package com.ilyastoletov.iqtest.presentation.vacancies.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ilyastoletov.domain.model.Sorting
import com.ilyastoletov.domain.model.Vacancy
import com.ilyastoletov.domain.util.Mock
import com.ilyastoletov.iqtest.presentation.theme.IQGroupTestTheme
import com.ilyastoletov.iqtest.presentation.vacancies.screen.components.FiltersSideSheet
import com.ilyastoletov.iqtest.presentation.vacancies.screen.components.SearchField
import com.ilyastoletov.iqtest.presentation.vacancies.screen.components.SortingBottomSheet
import com.ilyastoletov.iqtest.presentation.vacancies.screen.components.VacancyItem

@Composable
fun VacanciesScreen() {

    Content(Mock.testVacanciesList)

}

@Composable
private fun Content(
    vacancies: List<Vacancy>
) {

    var filtersSideSheetVisible by remember { mutableStateOf(false) }
    var sortingBottomSheetVisible by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            SearchField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = 16.dp),
                query = "",
                onQueryChange = {},
                onSearch = {},
                onClickFilters = { filtersSideSheetVisible = true },
                onClickSorting = { sortingBottomSheetVisible = true }
            )
        },
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

    if (filtersSideSheetVisible) {
        FiltersSideSheet(
            filters = Mock.testFilters,
            onClear = {},
            onClose = { filtersSideSheetVisible = false },
            onApplyFilters = {}
        )
    }

    if (sortingBottomSheetVisible) {
        SortingBottomSheet(
            selected = Sorting.RELEVANCE,
            onSelect = {},
            onDismiss = { sortingBottomSheetVisible = false }
        )
    }

}

@Preview(showBackground = true)
@Composable
private fun VacanciesScreenPreview() {
    IQGroupTestTheme {
        Content(
            vacancies = Mock.testVacanciesList
        )
    }
}