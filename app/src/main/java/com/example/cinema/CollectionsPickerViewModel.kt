package com.example.cinema

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.cinema.domain.model.Film
import com.example.cinema.domain.model.Collection
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

data class CollectionItemUi(
    val id: Long,
    val name: String,
    val count: Int,
    val isChecked: Boolean
)

class CollectionsPickerViewModel(
    private val repo: CollectionsRepository,
    private val film: Film
) : ViewModel() {
    private val _items = MutableStateFlow<List<CollectionItemUi>>(emptyList())
    val items: StateFlow<List<CollectionItemUi>> = _items

    init {
        viewModelScope.launch {
            repo.getCollections().collectLatest { collections ->
                val current = collections.map { c -> 
                    CollectionItemUi(id = c.id, name = c.name, count = 0, isChecked = false) 
                }
                _items.value = current
                
                // Update counts and checked flags
                collections.forEach { collection ->
                    launch {
                        repo.getCollectionCount(collection.id).collectLatest { cnt ->
                            _items.value = _items.value.map { 
                                if (it.id == collection.id) it.copy(count = cnt) else it 
                            }
                        }
                    }
                    launch {
                        val checked = repo.isInCollection(film.id, collection.id)
                        _items.value = _items.value.map { 
                            if (it.id == collection.id) it.copy(isChecked = checked) else it 
                        }
                    }
                }
            }
        }
    }

    fun toggle(collectionId: Long) {
        viewModelScope.launch {
            val after = repo.toggleInCollection(film, collectionId)
            _items.value = _items.value.map { 
                if (it.id == collectionId) it.copy(isChecked = after) else it 
            }
        }
    }

    fun createAndSelect(name: String) {
        viewModelScope.launch {
            val id = repo.createCustomCollection(name)
            repo.addFilmToCollection(film, id)
        }
    }
}

class CollectionsPickerViewModelFactory(
    private val repo: CollectionsRepository,
    private val film: Film
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CollectionsPickerViewModel::class.java)) {
            return CollectionsPickerViewModel(repo, film) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}


