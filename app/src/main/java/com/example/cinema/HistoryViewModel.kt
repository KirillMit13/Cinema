package com.example.cinema

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HistoryViewModel(private val historyDao: HistoryDao) : ViewModel() {
    val items: StateFlow<List<HistoryWithFilm>> = historyDao.getRecentWithFilmData(200)
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun clear() {
        viewModelScope.launch { historyDao.clearAll() }
    }
}

class HistoryViewModelFactory(private val historyDao: HistoryDao) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HistoryViewModel::class.java)) {
            return HistoryViewModel(historyDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}


