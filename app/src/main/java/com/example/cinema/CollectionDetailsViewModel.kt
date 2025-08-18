package com.example.cinema

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.cinema.domain.model.Collection
import com.example.cinema.domain.model.Film
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CollectionDetailsViewModel(
    private val repo: CollectionsRepository,
    private val collectionId: Long
) : ViewModel() {
    
    private val _collection = MutableStateFlow<Collection?>(null)
    val collection: StateFlow<Collection?> = _collection.asStateFlow()
    
    private val _films = MutableStateFlow<List<Film>>(emptyList())
    val films: StateFlow<List<Film>> = _films.asStateFlow()
    
    init {
        loadCollection()
        loadFilms()
    }
    
    private fun loadCollection() {
        viewModelScope.launch {
            repo.getCollection(collectionId).collect { collection ->
                _collection.value = collection
            }
        }
    }
    
    private fun loadFilms() {
        viewModelScope.launch {
            repo.getFilmsForCollection(collectionId).collect { films ->
                _films.value = films
            }
        }
    }
    
    fun removeFilm(filmId: Int) {
        viewModelScope.launch {
            repo.removeFilmFromCollection(filmId, collectionId)
        }
    }
}

class CollectionDetailsViewModelFactory(
    private val repo: CollectionsRepository,
    private val collectionId: Long
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CollectionDetailsViewModel::class.java)) {
            return CollectionDetailsViewModel(repo, collectionId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
