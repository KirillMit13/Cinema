package com.example.cinema

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import androidx.lifecycle.viewmodel.compose.viewModel


@Composable
fun SearchSettingsScreen(prefs: SearchPrefs, scope: CoroutineScope, onApply: () -> Unit) {
    val state by prefs.state.collectAsState(initial = SearchSettings("ALL", null, null, 1900, 2100, 0, 10, "RATING", false))
    val filtersVm: FiltersViewModel = viewModel(factory = FiltersViewModelFactory(com.example.cinema.data.remote.ApiClient.kinopoiskService))
    val countries by filtersVm.countries.collectAsState()
    val genres by filtersVm.genres.collectAsState()
    val selectedType = remember { mutableStateOf(state.type) }
    val selectedCountry = remember { mutableStateOf(state.countryId) }
    val selectedGenre = remember { mutableStateOf(state.genreId) }
    val hideWatched = remember { mutableStateOf(state.hideWatched) }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Тип: ${selectedType.value}")
        LazyRow(modifier = Modifier.padding(top = 8.dp)) {
            items(listOf("ALL" to "Искать все", "MOVIE" to "Только фильмы", "TV" to "Только сериалы")) { (value, label) ->
                AssistChip(onClick = { selectedType.value = value }, label = { Text(label) }, modifier = Modifier.padding(end = 8.dp))
            }
        }
        Text("Годы: ${state.yearFrom} - ${state.yearTo}")
        Row(modifier = Modifier.fillMaxWidth()) {
            Text("Рейтинг от ${state.ratingFrom}")
            Slider(value = state.ratingFrom.toFloat(), onValueChange = { /* no-op demo */ }, valueRange = 0f..10f)
        }
        if (countries.isNotEmpty()) {
            Text("Страна")
            LazyRow(modifier = Modifier.padding(top = 4.dp, bottom = 8.dp)) {
                items(countries.take(15)) { c ->
                    AssistChip(onClick = { selectedCountry.value = c.id }, label = { Text(c.name) }, modifier = Modifier.padding(end = 8.dp))
                }
            }
        }
        if (genres.isNotEmpty()) {
            Text("Жанр")
            LazyRow(modifier = Modifier.padding(top = 4.dp, bottom = 8.dp)) {
                items(genres.take(15)) { g ->
                    AssistChip(onClick = { selectedGenre.value = g.id }, label = { Text(g.name) }, modifier = Modifier.padding(end = 8.dp))
                }
            }
        }
        Row(modifier = Modifier.fillMaxWidth()) {
            Text("Скрывать просмотренные")
            Checkbox(checked = hideWatched.value, onCheckedChange = { hideWatched.value = it })
        }
        Button(onClick = {
            scope.launch {
                prefs.save(
                    state.copy(
                        type = selectedType.value,
                        countryId = selectedCountry.value,
                        genreId = selectedGenre.value,
                        hideWatched = hideWatched.value
                    )
                )
                onApply()
            }
        }) { Text("Применить") }
    }
}


