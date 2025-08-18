package com.example.cinema

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ProfileViewModel(
    filmDao: FilmDao,
    collectionsRepository: CollectionsRepository,
    historyDao: HistoryDao
) : ViewModel() {
    init {
        println("ProfileViewModel: Initializing...")
    }
    
    val watched: StateFlow<List<FilmEntity>> = filmDao.getWatchedFilms()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
        .also { flow ->
            viewModelScope.launch {
                flow.collect { films ->
                    println("ProfileViewModel: Watched films count: ${films.size}")
                }
            }
        }

    val collections = collectionsRepository.getCollections()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
        .also { flow ->
            viewModelScope.launch {
                flow.collect { collections ->
                    println("ProfileViewModel: Collections count: ${collections.size}")
                }
            }
        }

    val history = historyDao.getRecent(100)
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
        .also { flow ->
            viewModelScope.launch {
                flow.collect { history ->
                    println("ProfileViewModel: History count: ${history.size}")
                }
            }
        }
}

class ProfileViewModelFactory(
    private val filmDao: FilmDao,
    private val collectionsRepository: CollectionsRepository,
    private val historyDao: HistoryDao
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            return ProfileViewModel(filmDao, collectionsRepository, historyDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}


