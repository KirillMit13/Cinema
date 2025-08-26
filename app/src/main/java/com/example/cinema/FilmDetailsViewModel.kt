package com.example.cinema

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.cinema.data.mapper.toDomain
import com.example.cinema.data.mapper.toDomainPerson
import com.example.cinema.data.remote.model.KinopoiskService
import com.example.cinema.domain.model.FilmDetails
import com.example.cinema.domain.model.Person
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.async


class FilmDetailsViewModel(
    private val api: KinopoiskService,
    private val collectionsRepository: CollectionsRepository,
    private val filmId: Int,
    private val historyDao: HistoryDao
) : ViewModel() {
    private val _state = MutableStateFlow<FilmDetails?>(null)
    val state: StateFlow<FilmDetails?> = _state

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _staff = MutableStateFlow<List<Person>>(emptyList())
    val staff: StateFlow<List<Person>> = _staff

    private val _similars = MutableStateFlow<List<com.example.cinema.domain.model.Film>>(emptyList())
    val similars: StateFlow<List<com.example.cinema.domain.model.Film>> = _similars

    private val _isInAnyCollection = MutableStateFlow(false)
    val isInAnyCollection: StateFlow<Boolean> = _isInAnyCollection

    init {
        load()
    }

    fun load() {
        viewModelScope.launch {
            try {
                println("FilmDetailsViewModel: Starting to load film details for ID: $filmId")
                _error.value = null
                val detailsDeferred = async { api.getFilmDetails(filmId).toDomain() }
                val staffDeferred = async { api.getStaff(filmId).map { it.toDomainPerson() } }
                val similarsDeferred = async { api.getSimilars(filmId).items.map { it.toDomain() } }

                val details: FilmDetails = detailsDeferred.await()
                val staff: List<Person> = staffDeferred.await()
                val similars: List<com.example.cinema.domain.model.Film> = similarsDeferred.await()

                println("FilmDetailsViewModel: Successfully loaded film details")
                _state.value = details
                _staff.value = staff
                _similars.value = similars
                viewModelScope.launch {
                    collectionsRepository.isInAnyCollectionFlow(filmId).collect { inAny ->
                        _isInAnyCollection.value = inAny
                    }
                }
                runCatching {
                    historyDao.insert(
                        HistoryEntity(
                            type = "FILM",
                            refId = filmId,
                            timestamp = System.currentTimeMillis()
                        )
                    )
                }
            } catch (e: Exception) {
                println("FilmDetailsViewModel: Error loading film details: ${e.message}")
                e.printStackTrace()
                _error.value = e.message
            }
        }
    }

    fun toggleFavorite() {
        val details = _state.value ?: return
        viewModelScope.launch {
            collectionsRepository.toggleFavorite(
                com.example.cinema.domain.model.Film(
                    id = details.id,
                    title = details.title,
                    posterUrl = details.posterUrl,
                    rating = details.rating,
                    year = details.year,
                    genres = details.genres,
                    isWatched = false
                )
            )
            _state.value = details.copy(isFavorite = !details.isFavorite)
        }
    }

    fun toggleWatchlist() {
        val details = _state.value ?: return
        viewModelScope.launch {
            collectionsRepository.toggleWatchlist(
                com.example.cinema.domain.model.Film(
                    id = details.id,
                    title = details.title,
                    posterUrl = details.posterUrl,
                    rating = details.rating,
                    year = details.year,
                    genres = details.genres,
                    isWatched = false
                )
            )
        }
    }

    fun toggleWatched() {
        val details = _state.value ?: return
        viewModelScope.launch {
            collectionsRepository.toggleWatched(
                com.example.cinema.domain.model.Film(
                    id = details.id,
                    title = details.title,
                    posterUrl = details.posterUrl,
                    rating = details.rating,
                    year = details.year,
                    genres = details.genres,
                    isWatched = false
                )
            )
        }
    }
}

class FilmDetailsViewModelFactory(
    private val api: KinopoiskService,
    private val collectionsRepository: CollectionsRepository,
    private val filmId: Int,
    private val historyDao: HistoryDao
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FilmDetailsViewModel::class.java)) {
            return FilmDetailsViewModel(api, collectionsRepository, filmId, historyDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}


