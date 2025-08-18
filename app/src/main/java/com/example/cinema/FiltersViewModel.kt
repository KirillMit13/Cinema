package com.example.cinema

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.cinema.data.remote.model.KinopoiskService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FiltersViewModel(private val api: KinopoiskService) : ViewModel() {
    private val _countries = MutableStateFlow<List<com.example.cinema.data.remote.model.CountryItem>>(emptyList())
    val countries: StateFlow<List<com.example.cinema.data.remote.model.CountryItem>> = _countries

    private val _genres = MutableStateFlow<List<com.example.cinema.data.remote.model.GenreItem>>(emptyList())
    val genres: StateFlow<List<com.example.cinema.data.remote.model.GenreItem>> = _genres

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    init {
        viewModelScope.launch {
            runCatching { api.getFilters() }
                .onSuccess {
                    _countries.value = it.countries
                    _genres.value = it.genres
                }
                .onFailure { _error.value = it.message }
        }
    }
}

class FiltersViewModelFactory(private val api: KinopoiskService) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FiltersViewModel::class.java)) {
            return FiltersViewModel(api) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}


