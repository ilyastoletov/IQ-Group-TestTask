package com.ilyastoletov.iqtest.presentation.vacancies.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.IconButton
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import com.ilyastoletov.domain.model.Sorting
import com.ilyastoletov.domain.model.Vacancy
import com.ilyastoletov.domain.model.filter.AppliedFilters
import com.ilyastoletov.domain.model.filter.Filter
import com.ilyastoletov.domain.util.Mock
import com.ilyastoletov.iqtest.presentation.theme.IQGroupTestTheme
import com.ilyastoletov.iqtest.presentation.vacancies.screen.components.FiltersSideSheet
import com.ilyastoletov.iqtest.presentation.vacancies.screen.components.SearchField
import com.ilyastoletov.iqtest.presentation.vacancies.screen.components.SortingBottomSheet
import com.ilyastoletov.iqtest.presentation.vacancies.screen.components.VacancyItem
import com.ilyastoletov.iqtest.presentation.vacancies.viewmodel.VacanciesViewModel
import com.ilyastoletov.iqtest.R
import com.ilyastoletov.iqtest.presentation.extension.toggleItem
import com.ilyastoletov.iqtest.presentation.shared.model.LoadingState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.emptyFlow

@Composable
fun VacanciesScreen(
    viewModel: VacanciesViewModel,
    openFavouritesScreen: () -> Unit
) {

    val sorting by viewModel.selectedSorting.collectAsState()

    val filters by viewModel.availableFilters.collectAsState()
    val appliedFilters by viewModel.selectedFilters.collectAsState()
    val filterLoadingState by viewModel.filtersLoadingState.collectAsState()

    val vacancies = viewModel.vacancies.collectAsLazyPagingItems(context = Dispatchers.IO)

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.snackbarMessage.collectLatest { message ->
            if (message != null) {
                snackbarHostState.showSnackbar(message)
            }
        }
    }

    Content(
        snackbarHostState = snackbarHostState,
        vacancies = vacancies,
        filters = filters,
        appliedFilters = appliedFilters,
        filtersLoadingState = filterLoadingState,
        selectedSorting = sorting,
        onSearch = { query -> viewModel.searchVacancies(query) },
        onApplyFilters = { newFilters -> viewModel.applyFilters(newFilters) },
        onClearFilters = { viewModel.clearFilters() },
        onChangeSorting = { sort -> viewModel.changeSorting(sort) },
        onClickFavourite = { vacancy -> viewModel.toggleFavourite(vacancy) },
        onOpenFavourites = openFavouritesScreen,
        onRefresh = { viewModel.refresh() }
    )

}


@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun Content(
    snackbarHostState: SnackbarHostState,
    vacancies: LazyPagingItems<Vacancy>,
    filters: Filter,
    appliedFilters: AppliedFilters,
    filtersLoadingState: LoadingState,
    selectedSorting: Sorting,
    onSearch: (String) -> Unit,
    onApplyFilters: (AppliedFilters) -> Unit,
    onClearFilters: () -> Unit,
    onChangeSorting: (Sorting) -> Unit,
    onClickFavourite: (Vacancy) -> Unit,
    onOpenFavourites: () -> Unit,
    onRefresh: () -> Unit
) {

    var filtersSideSheetVisible by remember { mutableStateOf(false) }
    var sortingBottomSheetVisible by remember { mutableStateOf(false) }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            val focusManager = LocalFocusManager.current
            var searchQuery by rememberSaveable { mutableStateOf("") }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = 16.dp)
            ) {
                SearchField(
                    modifier = Modifier.weight(0.9f),
                    query = searchQuery,
                    onQueryChange = { searchQuery = it },
                    onSearch = {
                        onSearch.invoke(searchQuery)
                        focusManager.clearFocus()
                    },
                    onClickFilters = { filtersSideSheetVisible = true },
                    onClickSorting = { sortingBottomSheetVisible = true }
                )
                Spacer(
                    modifier = Modifier.width(16.dp)
                )
                IconButton(
                    modifier = Modifier.weight(0.1f),
                    onClick = onOpenFavourites
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_star_outlined),
                        tint = MaterialTheme.colorScheme.outlineVariant,
                        contentDescription = null
                    )
                }
            }

        },
    ) { scaffoldPadding ->

        val isRefreshing by remember {
            derivedStateOf {
                !vacancies.loadState.isIdle && vacancies.loadState.append !is LoadState.Loading
            }
        }

        val state = rememberPullRefreshState(
            refreshing = isRefreshing,
            onRefresh = onRefresh
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = scaffoldPadding.calculateTopPadding())
                .pullRefresh(state)
        ) {
            PagedVacanciesList(
                modifier = Modifier.fillMaxSize(),
                vacancies = vacancies,
                onClickFavourite = onClickFavourite
            )
            PullRefreshIndicator(
                modifier = Modifier.align(Alignment.TopCenter),
                refreshing = isRefreshing,
                state = state,
                contentColor = MaterialTheme.colorScheme.secondary
            )
        }

    }

    AnimatedVisibility(
        visible = filtersSideSheetVisible,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        FiltersSideSheet(
            filters = filters,
            appliedFilters = appliedFilters,
            loadingState = filtersLoadingState,
            onClear = onClearFilters,
            onApplyFilters = onApplyFilters,
            onClose = { filtersSideSheetVisible = false },
        )
    }

    if (sortingBottomSheetVisible) {
        SortingBottomSheet(
            selected = selectedSorting,
            onSelect = onChangeSorting,
            onDismiss = { sortingBottomSheetVisible = false }
        )
    }

}

@Composable
private fun PagedVacanciesList(
    modifier: Modifier = Modifier,
    vacancies: LazyPagingItems<Vacancy>,
    onClickFavourite: (Vacancy) -> Unit
) {
    val localFavouritesList = remember { mutableStateListOf<Vacancy>() }

    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(
            bottom = 16.dp,
            start = 16.dp,
            end = 16.dp
        ),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        items(
            count = vacancies.itemCount,
            key = vacancies.itemKey { it.id },
            contentType = vacancies.itemContentType { it::class.simpleName }
        ) { index ->
            val item = vacancies.get(index)

            item?.let { vacancy ->
                VacancyItem(
                    title = vacancy.title,
                    salary = vacancy.salary,
                    company = vacancy.company,
                    location = vacancy.location,
                    isFavourite = (vacancy.isFavourite || vacancy in localFavouritesList),
                    onClickFavourite = {
                        localFavouritesList.toggleItem(vacancy)
                        onClickFavourite(item)
                    }
                )
            }
        }

        if (vacancies.loadState.isIdle && vacancies.itemCount == 0) {
            item {
                Text(
                    text = stringResource(R.string.vacancies_list_no_results),
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(all = 12.dp)
                        .fillMaxWidth()
                )
            }
        }

    }
}

@Preview(showBackground = true)
@Composable
private fun VacanciesScreenPreview() {
    IQGroupTestTheme {
        val emptyVacanciesLazyPagingItems =
            emptyFlow<PagingData<Vacancy>>().collectAsLazyPagingItems()
        Content(
            snackbarHostState = remember { SnackbarHostState() },
            vacancies = emptyVacanciesLazyPagingItems,
            selectedSorting = Sorting.RELEVANCE,
            filters = Mock.testFilter,
            filtersLoadingState = LoadingState.LOADED,
            appliedFilters = AppliedFilters(),
            onSearch = {},
            onApplyFilters = {},
            onClearFilters = {},
            onChangeSorting = {},
            onRefresh = {},
            onClickFavourite = {},
            onOpenFavourites = {}
        )
    }
}