package com.example.cinema

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.cinema.data.mapper.toDomain
import com.example.cinema.data.remote.model.KinopoiskService
import com.example.cinema.domain.model.Film
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*

class SearchViewModel(
    private val api: KinopoiskService
) : ViewModel() {
    
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()
    
    private val _isSearching = MutableStateFlow(false)
    val isSearching: StateFlow<Boolean> = _isSearching.asStateFlow()
    
    private val _searchState = MutableStateFlow<List<Film>>(emptyList())
    val searchState: StateFlow<List<Film>> = _searchState.asStateFlow()
    
    init {
        setupSearchFlow()
    }
    
    @OptIn(FlowPreview::class)
    private fun setupSearchFlow() {
        _searchQuery
            .debounce(500)
            .filter { it.length >= 2 }
            .distinctUntilChanged()
            .onEach { _isSearching.value = true }
            .flatMapLatest { query ->
                performSearch(query)
            }
            .onEach { result ->
                _searchState.value = result
                _isSearching.value = false
            }
            .catch { error ->
                _isSearching.value = false
            }
            .launchIn(viewModelScope)
    }
    
    fun search(query: String) {
        _searchQuery.value = query
    }
    
    fun clearSearch() {
        _searchQuery.value = ""
        _searchState.value = emptyList()
    }
    
    private fun performSearch(query: String): Flow<List<Film>> = flow {
        try {
            val searchResponse = api.searchFilms(query = query, page = 1)
            val films = searchResponse.items.map { it.toDomain() }
            emit(films)
        } catch (e: Exception) {
            emit(emptyList())
        }
    }
}

class SearchViewModelFactory(
    private val api: KinopoiskService
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchViewModel::class.java)) {
            return SearchViewModel(api) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}


