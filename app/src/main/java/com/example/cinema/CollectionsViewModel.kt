package com.example.cinema

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.cinema.domain.model.Collection
import com.example.cinema.domain.model.Film
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.launch

class CollectionsViewModel(
    private val repo: CollectionsRepository
) : ViewModel() {
    
    private val _collections = MutableStateFlow<List<Collection>>(emptyList())
    val collections: StateFlow<List<Collection>> = _collections.asStateFlow()
    
    init {
        loadCollections()
    }
    
    private fun loadCollections() {
        viewModelScope.launch {
            repo.getCollections().collect { list ->
                // Подтягиваем актуальные счётчики фильмов
                list.forEach { c ->
                    launch {
                        repo.getCollectionCount(c.id).collect { count ->
                            _collections.value = _collections.value
                                .map { if (it.id == c.id) it.copy(filmCount = count) else it }
                                .ifEmpty { list.map { it.copy(filmCount = if (it.id == c.id) count else 0) } }
                        }
                    }
                }
                if (_collections.value.isEmpty()) {
                    _collections.value = list
                }
            }
        }
    }
    
    fun createCustom(name: String = "Новая коллекция") {
        viewModelScope.launch { repo.createCustomCollection(name) }
    }
    
    fun deleteCustom(id: Long) {
        viewModelScope.launch { repo.deleteCustomCollection(id) }
    }
    
    fun toggleFavorite(film: com.example.cinema.domain.model.Film) {
        viewModelScope.launch { repo.toggleFavorite(film) }
    }
    
    fun toggleWatchlist(film: com.example.cinema.domain.model.Film) {
        viewModelScope.launch { repo.toggleWatchlist(film) }
    }
    
    fun toggleWatched(film: com.example.cinema.domain.model.Film) {
        viewModelScope.launch { repo.toggleWatched(film) }
    }
    
    fun addFilmToCollection(film: com.example.cinema.domain.model.Film, collectionId: Long) {
        viewModelScope.launch { repo.addFilmToCollection(film, collectionId) }
    }
    
    fun removeFilmFromCollection(filmId: Int, collectionId: Long) {
        viewModelScope.launch { repo.removeFilmFromCollection(filmId, collectionId) }
    }
    
    fun toggleInCollection(film: com.example.cinema.domain.model.Film, collectionId: Long) {
        viewModelScope.launch { repo.toggleInCollection(film, collectionId) }
    }
}

class CollectionsViewModelFactory(private val repo: CollectionsRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CollectionsViewModel::class.java)) {
            return CollectionsViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}




