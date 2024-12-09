package com.ilyastoletov.iqtest.presentation.vacancies.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ilyastoletov.domain.model.Sorting
import com.ilyastoletov.domain.model.filter.Filter
import com.ilyastoletov.domain.model.filter.FilterMap
import com.ilyastoletov.domain.usecase.GetPagedVacanciesUseCase
import com.ilyastoletov.domain.usecase.LoadFiltersUseCase
import com.ilyastoletov.iqtest.presentation.vacancies.viewmodel.model.FiltersLoadingState
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
    private val getPagedVacanciesUseCase: GetPagedVacanciesUseCase
) : ViewModel() {

    private val refreshPull = MutableStateFlow(false)

    private val searchQuery = MutableStateFlow("")

    private val _availableFilters = MutableStateFlow(Filter())
    val availableFilters = _availableFilters.asStateFlow()

    private val _filtersLoadingState = MutableStateFlow(FiltersLoadingState.LOADING)
    val filtersLoadingState = _filtersLoadingState.asStateFlow()

    private val _selectedFilters = MutableStateFlow<FilterMap>(mapOf())
    val selectedFilters = _selectedFilters.asStateFlow()

    private val _selectedSorting = MutableStateFlow(Sorting.RELEVANCE)
    val selectedSorting = _selectedSorting.asStateFlow()

    private val _snackbarMessage = MutableSharedFlow<String?>()
    val snackbarMessage = _snackbarMessage.asSharedFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val vacancies = combine(searchQuery, _selectedFilters, _selectedSorting, refreshPull) { query, filters, sorting, _ ->
        Triple(query, filters, sorting)
    }.flatMapLatest { (query, filters, sort) ->
        getPagedVacanciesUseCase.invoke(query, filters, sort, viewModelScope)
    }

    init {
        loadFilters()
    }

    private fun loadFilters() {
        viewModelScope.launch(Dispatchers.IO) {
            loadFiltersUseCase.invoke().fold(
                onSuccess = { filtersMap ->
                    _availableFilters.emit(filtersMap)
                    _filtersLoadingState.emit(FiltersLoadingState.LOADED)
                },
                onFailure = { error ->
                    _snackbarMessage.emit("Ошибка загрузки фильтров: ${error.message}")
                    _filtersLoadingState.emit(FiltersLoadingState.ERROR)
                }
            )
        }
    }

    fun searchVacancies(query: String) {
        searchQuery.update { query }
    }

    fun applyFilters(filters: FilterMap) {
        _selectedFilters.update { filters }
    }

    fun clearFilters() {
        _selectedFilters.update { emptyMap() }
    }

    fun changeSorting(sorting: Sorting) {
        _selectedSorting.update { sorting }
    }

    fun refresh() {
        refreshPull.update { !it }
    }

}