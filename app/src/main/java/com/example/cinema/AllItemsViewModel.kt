package com.example.cinema

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.cinema.domain.model.Film
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class AllItemsViewModel(
    private val repository: HomeRepository,
    private val sectionTitle: String
) : ViewModel() {
    private val _items = MutableStateFlow<List<Film>>(emptyList())
    val items: StateFlow<List<Film>> = _items

    init {
        viewModelScope.launch {
            val sections = repository.loadHomeSections()
            _items.value = sections.firstOrNull { it.sectionTitle == sectionTitle }?.films ?: emptyList()
        }
    }
}


