package com.example.cinema

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.cinema.data.remote.model.KinopoiskService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class GalleryViewModel(private val api: KinopoiskService, private val filmId: Int) : ViewModel() {
    private val _types = MutableStateFlow<List<String>>(emptyList())
    val types: StateFlow<List<String>> = _types

    private val _images = MutableStateFlow<List<String>>(emptyList())
    val images: StateFlow<List<String>> = _images

    init { load(null) }

    fun load(type: String?) {
        viewModelScope.launch {
            val response = api.getImages(id = filmId, type = type, page = 1)
            _types.value = response.items.mapNotNull { it.type }.distinct()
            _images.value = response.items.map { it.previewUrl }
        }
    }
}

class GalleryViewModelFactory(private val api: KinopoiskService, private val filmId: Int) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GalleryViewModel::class.java)) {
            return GalleryViewModel(api, filmId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}


