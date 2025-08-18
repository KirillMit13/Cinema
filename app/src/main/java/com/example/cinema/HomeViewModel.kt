package com.example.cinema

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: HomeRepository
) : ViewModel() {
    private val _sections = MutableStateFlow<List<FilmSection>>(emptyList())
    val sections: StateFlow<List<FilmSection>> = _sections.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                println("HomeViewModel: Starting to load data...")
                val sections = repository.loadHomeSections()
                println("HomeViewModel: Loaded ${sections.size} sections")
                sections.forEach { section ->
                    println("HomeViewModel: Section '${section.sectionTitle}' has ${section.films.size} films")
                }
                _sections.value = sections
            } catch (e: Exception) {
                println("HomeViewModel: Error loading data: ${e.message}")
                e.printStackTrace()
                _errorMessage.value = e.message ?: "Ошибка загрузки"
            } finally {
                _isLoading.value = false
            }
        }
    }
}