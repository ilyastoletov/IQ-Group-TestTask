package com.ilyastoletov.iqtest.presentation.favourites.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ilyastoletov.domain.model.Vacancy
import com.ilyastoletov.domain.usecase.ClearFavouritesUseCase
import com.ilyastoletov.domain.usecase.GetAllFavouritesUseCase
import com.ilyastoletov.domain.usecase.ToggleFavouriteUseCase
import com.ilyastoletov.iqtest.presentation.shared.model.LoadingState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavouritesViewModel @Inject constructor(
    private val getAllFavouritesUseCase: GetAllFavouritesUseCase,
    private val clearFavouritesUseCase: ClearFavouritesUseCase,
    private val toggleFavouriteUseCase: ToggleFavouriteUseCase
) : ViewModel() {

    private val _vacancies = MutableStateFlow<List<Vacancy>>(listOf())
    val vacancies = _vacancies.asStateFlow()

    private val _loadingState = MutableStateFlow(LoadingState.LOADING)
    val loadingState = _loadingState.asStateFlow()

    private val _snackbarMessage = MutableSharedFlow<String?>()
    val snackbarMessage = _snackbarMessage.asSharedFlow()

    fun loadFavouriteVacancies() {
        viewModelScope.launch(Dispatchers.IO) {
            getAllFavouritesUseCase.invoke().fold(
                onSuccess = { list ->
                    _vacancies.emit(list)
                    _loadingState.emit(LoadingState.LOADED)
                },
                onFailure = {
                    _snackbarMessage.emit(it.message)
                }
            )
        }
    }

    fun removeItem(model: Vacancy) {
        viewModelScope.launch(Dispatchers.IO) {
            toggleFavouriteUseCase.invoke(model)
                .fold(
                    onSuccess = {
                        _vacancies.update { list ->
                            list
                                .toMutableList()
                                .apply { remove(model) }
                        }
                    },
                    onFailure = {
                        _snackbarMessage.emit(it.message)
                    }
                )
        }
    }

    fun clearList() {
        viewModelScope.launch(Dispatchers.IO) {
            clearFavouritesUseCase.invoke()
                .fold(
                    onSuccess = {
                        _vacancies.update { emptyList() }
                    },
                    onFailure = {
                        _snackbarMessage.emit(it.message)
                    }
                )
        }
    }

}