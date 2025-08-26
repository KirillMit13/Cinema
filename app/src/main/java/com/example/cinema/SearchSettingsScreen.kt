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
import androidx.compose.material3.AssistChipDefaults
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
import androidx.compose.material3.RangeSlider
import androidx.compose.runtime.LaunchedEffect

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

    val ratingRange = remember { mutableStateOf(state.ratingFrom.toFloat()..state.ratingTo.toFloat()) }
    LaunchedEffect(state.ratingFrom, state.ratingTo) {
        ratingRange.value = state.ratingFrom.toFloat()..state.ratingTo.toFloat()
    }

    val decades = remember {
        (1900..2100 step 10).toList()
    }
    val startDecade = remember { mutableStateOf(state.yearFrom - (state.yearFrom % 10)) }
    val endDecade = remember { mutableStateOf(state.yearTo - (state.yearTo % 10)) }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Тип: ${selectedType.value}")
        LazyRow(modifier = Modifier.padding(top = 8.dp)) {
            val typeOptions = listOf("ALL" to "Искать все", "MOVIE" to "Только фильмы", "TV" to "Только сериалы")
            items(typeOptions) { (value, label) ->
                val selected = selectedType.value == value
                AssistChip(
                    onClick = { selectedType.value = value },
                    label = { Text(label) },
                    modifier = Modifier.padding(end = 8.dp),
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = if (selected) androidx.compose.material3.MaterialTheme.colorScheme.primary else androidx.compose.material3.MaterialTheme.colorScheme.surfaceVariant,
                        labelColor = if (selected) androidx.compose.material3.MaterialTheme.colorScheme.onPrimary else androidx.compose.material3.MaterialTheme.colorScheme.onSurface
                    )
                )
            }
        }

        Text("Годы: ${startDecade.value} - ${endDecade.value}", modifier = Modifier.padding(top = 16.dp, bottom = 8.dp))
        Text("Начало десятилетия")
        LazyRow(modifier = Modifier.padding(top = 4.dp, bottom = 8.dp)) {
            items(decades) { d ->
                val selected = startDecade.value == d
                AssistChip(
                    onClick = {
                        startDecade.value = d
                        if (endDecade.value < d) endDecade.value = d
                    },
                    label = { Text(d.toString()) },
                    modifier = Modifier.padding(end = 8.dp),
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = if (selected) androidx.compose.material3.MaterialTheme.colorScheme.primary else androidx.compose.material3.MaterialTheme.colorScheme.surfaceVariant,
                        labelColor = if (selected) androidx.compose.material3.MaterialTheme.colorScheme.onPrimary else androidx.compose.material3.MaterialTheme.colorScheme.onSurface
                    )
                )
            }
        }
        Text("Конец десятилетия")
        LazyRow(modifier = Modifier.padding(top = 4.dp, bottom = 8.dp)) {
            items(decades) { d ->
                val selected = endDecade.value == d
                val enabled = d >= startDecade.value
                AssistChip(
                    onClick = { if (enabled) endDecade.value = d },
                    label = { Text(d.toString()) },
                    modifier = Modifier.padding(end = 8.dp),
                    enabled = enabled,
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = if (selected) androidx.compose.material3.MaterialTheme.colorScheme.primary else androidx.compose.material3.MaterialTheme.colorScheme.surfaceVariant,
                        labelColor = if (selected) androidx.compose.material3.MaterialTheme.colorScheme.onPrimary else androidx.compose.material3.MaterialTheme.colorScheme.onSurface
                    )
                )
            }
        }

        Text("Рейтинг: ${ratingRange.value.start.toInt()} - ${ratingRange.value.endInclusive.toInt()}", modifier = Modifier.padding(top = 8.dp))
        RangeSlider(
            value = ratingRange.value,
            onValueChange = { range ->
                val start = range.start.coerceIn(0f, 10f)
                val end = range.endInclusive.coerceIn(0f, 10f)
                ratingRange.value = start..maxOf(start, end)
            },
            valueRange = 0f..10f,
            steps = 9,
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
        )

        if (countries.isNotEmpty()) {
            Text("Страна", modifier = Modifier.padding(top = 8.dp))
            LazyRow(modifier = Modifier.padding(top = 4.dp, bottom = 8.dp)) {
                items(countries) { c ->
                    val selected = selectedCountry.value == c.id
                    AssistChip(
                        onClick = { selectedCountry.value = c.id },
                        label = { Text(c.name) },
                        modifier = Modifier.padding(end = 8.dp),
                        colors = AssistChipDefaults.assistChipColors(
                            containerColor = if (selected) androidx.compose.material3.MaterialTheme.colorScheme.primary else androidx.compose.material3.MaterialTheme.colorScheme.surfaceVariant,
                            labelColor = if (selected) androidx.compose.material3.MaterialTheme.colorScheme.onPrimary else androidx.compose.material3.MaterialTheme.colorScheme.onSurface
                        )
                    )
                }
            }
        }
        if (genres.isNotEmpty()) {
            Text("Жанр")
            LazyRow(modifier = Modifier.padding(top = 4.dp, bottom = 8.dp)) {
                items(genres) { g ->
                    val selected = selectedGenre.value == g.id
                    AssistChip(
                        onClick = { selectedGenre.value = g.id },
                        label = { Text(g.name) },
                        modifier = Modifier.padding(end = 8.dp),
                        colors = AssistChipDefaults.assistChipColors(
                            containerColor = if (selected) androidx.compose.material3.MaterialTheme.colorScheme.primary else androidx.compose.material3.MaterialTheme.colorScheme.surfaceVariant,
                            labelColor = if (selected) androidx.compose.material3.MaterialTheme.colorScheme.onPrimary else androidx.compose.material3.MaterialTheme.colorScheme.onSurface
                        )
                    )
                }
            }
        }

        Row(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp), verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
            Text("Скрывать просмотренные", modifier = Modifier.weight(1f))
            Checkbox(checked = hideWatched.value, onCheckedChange = { hideWatched.value = it })
        }

        Button(onClick = {
            scope.launch {
                val rf = ratingRange.value.start
                val rt = ratingRange.value.endInclusive
                prefs.save(
                    state.copy(
                        type = selectedType.value,
                        countryId = selectedCountry.value,
                        genreId = selectedGenre.value,
                        yearFrom = startDecade.value,
                        yearTo = endDecade.value + 9,
                        ratingFrom = rf.toInt(),
                        ratingTo = rt.toInt(),
                        hideWatched = hideWatched.value
                    )
                )
                onApply()
            }
        }) { Text("Применить") }
    }
}


