package com.example.cinema

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.cinema.data.remote.model.KinopoiskService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SeasonsViewModel(private val api: KinopoiskService, private val filmId: Int) : ViewModel() {
    private val _seasons = MutableStateFlow<List<com.example.cinema.data.remote.model.SeasonItem>>(emptyList())
    val seasons: StateFlow<List<com.example.cinema.data.remote.model.SeasonItem>> = _seasons

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    init { load() }

    private fun load() {
        viewModelScope.launch {
            runCatching { api.getSeasons(filmId).items }
                .onSuccess { _seasons.value = it }
                .onFailure { _error.value = it.message }
        }
    }
}

class SeasonsViewModelFactory(private val api: KinopoiskService, private val filmId: Int) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SeasonsViewModel::class.java)) {
            return SeasonsViewModel(api, filmId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}


