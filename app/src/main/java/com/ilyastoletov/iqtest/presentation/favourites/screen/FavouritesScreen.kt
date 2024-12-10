package com.ilyastoletov.iqtest.presentation.favourites.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ilyastoletov.domain.model.Vacancy
import com.ilyastoletov.iqtest.R
import com.ilyastoletov.iqtest.presentation.shared.model.LoadingState
import com.ilyastoletov.iqtest.presentation.favourites.viewmodel.FavouritesViewModel
import com.ilyastoletov.iqtest.presentation.shared.ClearTextButton
import com.ilyastoletov.iqtest.presentation.vacancies.screen.components.VacancyItem
import kotlinx.coroutines.flow.collectLatest

@Composable
fun FavouritesScreen(
    viewModel: FavouritesViewModel,
    onBack: () -> Unit
) {

    val favouriteVacancies by viewModel.vacancies.collectAsState()
    val loadingState by viewModel.loadingState.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.loadFavouriteVacancies()
    }

    LaunchedEffect(Unit) {
        viewModel.snackbarMessage.collectLatest { message ->
            if (message != null) {
                snackbarHostState.showSnackbar(message)
            }
        }
    }

    Content(
        favouriteVacancies = favouriteVacancies,
        loadingState = loadingState,
        onRemoveItem = { viewModel.removeItem(it) },
        onClearList = { viewModel.clearList() },
        onBack = onBack
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Content(
    favouriteVacancies: List<Vacancy>,
    loadingState: LoadingState,
    onRemoveItem: (Vacancy) -> Unit,
    onClearList: () -> Unit,
    onBack: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.favourites_screen_title),
                        style = MaterialTheme.typography.titleMedium
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBack
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.ic_arrow_back),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            contentDescription = null
                        )
                    }
                },
                actions = {
                    ClearTextButton(onClearList)
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.surface
    ) { scaffoldPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(scaffoldPadding)
        ) {
            when(loadingState) {

                LoadingState.LOADING -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = MaterialTheme.colorScheme.secondary
                    )
                }

                LoadingState.LOADED -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(all = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {

                        if (favouriteVacancies.isNotEmpty()) {
                            items(favouriteVacancies) { vacancy ->
                                VacancyItem(
                                    title = vacancy.title,
                                    salary = vacancy.salary,
                                    company = vacancy.company,
                                    location = vacancy.location,
                                    isFavourite = true,
                                    onClickFavourite = { onRemoveItem(vacancy) }
                                )
                            }
                        } else {
                            item {
                                Text(
                                    text = stringResource(R.string.favourites_list_empty),
                                    style = MaterialTheme.typography.titleMedium,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }

                    }
                }

                LoadingState.ERROR -> {
                    Text(
                        text = stringResource(R.string.loading_error_message),
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
    }
}