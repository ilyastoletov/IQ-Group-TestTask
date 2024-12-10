package com.ilyastoletov.iqtest.presentation.vacancies.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ilyastoletov.domain.model.Sorting
import com.ilyastoletov.domain.model.Vacancy
import com.ilyastoletov.domain.model.filter.AppliedFilters
import com.ilyastoletov.domain.model.filter.Filter
import com.ilyastoletov.domain.usecase.GetPagedVacanciesUseCase
import com.ilyastoletov.domain.usecase.LoadFiltersUseCase
import com.ilyastoletov.domain.usecase.ToggleFavouriteUseCase
import com.ilyastoletov.iqtest.presentation.shared.model.LoadingState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VacanciesViewModel @Inject constructor(
    private val loadFiltersUseCase: LoadFiltersUseCase,
    private val toggleFavouriteUseCase: ToggleFavouriteUseCase,
    private val getPagedVacanciesUseCase: GetPagedVacanciesUseCase
) : ViewModel() {

    private val refreshPull = MutableStateFlow(false)

    private val searchQuery = MutableStateFlow("")

    private val _availableFilters = MutableStateFlow(Filter())
    val availableFilters = _availableFilters.asStateFlow()

    private val _filtersLoadingState = MutableStateFlow(LoadingState.LOADING)
    val filtersLoadingState = _filtersLoadingState.asStateFlow()

    private val _selectedFilters = MutableStateFlow(AppliedFilters())
    val selectedFilters = _selectedFilters.asStateFlow()

    private val _selectedSorting = MutableStateFlow(Sorting.RELEVANCE)
    val selectedSorting = _selectedSorting.asStateFlow()

    private val _snackbarMessage = MutableSharedFlow<String?>()
    val snackbarMessage = _snackbarMessage.asSharedFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val vacancies = combine(searchQuery, _selectedFilters, _selectedSorting, refreshPull) { query, filters, sorting, _ ->
        Triple(query, filters, sorting)
    }.flatMapLatest { (query, filters, sort) ->
        getPagedVacanciesUseCase.invoke(
            searchQuery = query,
            filters = filters,
            sorting = sort,
            cacheScope = viewModelScope
        )
    }

    init {
        loadFilters()
    }

    private fun loadFilters() {
        viewModelScope.launch(Dispatchers.IO) {
            loadFiltersUseCase.invoke().fold(
                onSuccess = { filtersMap ->
                    _availableFilters.emit(filtersMap)
                    _filtersLoadingState.emit(LoadingState.LOADED)
                },
                onFailure = { error ->
                    _snackbarMessage.emit("Ошибка загрузки фильтров: ${error.message}")
                    _filtersLoadingState.emit(LoadingState.ERROR)
                }
            )
        }
    }

    fun searchVacancies(query: String) {
        searchQuery.update { query }
    }

    fun applyFilters(filters: AppliedFilters) {
        _selectedFilters.update { filters }
    }

    fun clearFilters() {
        _selectedFilters.update { AppliedFilters() }
    }

    fun changeSorting(sorting: Sorting) {
        _selectedSorting.update { sorting }
    }

    fun toggleFavourite(model: Vacancy) {
        viewModelScope.launch(Dispatchers.IO) {
            toggleFavouriteUseCase.invoke(model)
                .onFailure {
                    _snackbarMessage.emit(it.message)
                }
        }
    }

    fun refresh() {
        refreshPull.update { !it }
    }

}