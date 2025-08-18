package com.example.cinema

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.cinema.data.mapper.toBestFilms
import com.example.cinema.data.remote.model.KinopoiskService
import com.example.cinema.domain.model.Film
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ActorViewModel(private val api: KinopoiskService, private val personId: Int) : ViewModel() {
    private val _name = MutableStateFlow("")
    val name: StateFlow<String> = _name

    private val _profession = MutableStateFlow("")
    val profession: StateFlow<String> = _profession

    private val _poster = MutableStateFlow<String?>(null)
    val poster: StateFlow<String?> = _poster

    private val _best = MutableStateFlow<List<Film>>(emptyList())
    val best: StateFlow<List<Film>> = _best

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    init { load() }

    private fun load() {
        viewModelScope.launch {
            try {
                _error.value = null
                val person = api.getPerson(personId)
                _name.value = person.nameRu ?: person.nameEn ?: ""
                _profession.value = person.profession ?: ""
                _poster.value = person.posterUrl
                _best.value = person.toBestFilms()
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }
}

class ActorViewModelFactory(private val api: KinopoiskService, private val personId: Int) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ActorViewModel::class.java)) {
            return ActorViewModel(api, personId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}


