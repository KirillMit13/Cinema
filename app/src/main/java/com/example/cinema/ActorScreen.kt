package com.example.cinema

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun ActorScreen(viewModel: ActorViewModel) {
    val name by viewModel.name.collectAsState()
    val profession by viewModel.profession.collectAsState()
    val poster by viewModel.poster.collectAsState()
    val best by viewModel.best.collectAsState()

    Column(modifier = Modifier.padding(16.dp)) {
        AsyncImage(model = poster, contentDescription = name)
        Text(name, style = MaterialTheme.typography.headlineSmall)
        Text(profession)
        Text("Лучшие работы", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(top = 16.dp))
        LazyRow {
            items(best) { film ->
                Column(modifier = Modifier.padding(8.dp)) {
                    AsyncImage(model = film.posterUrl, contentDescription = film.title)
                    Text(film.title)
                }
            }
        }
    }
}


