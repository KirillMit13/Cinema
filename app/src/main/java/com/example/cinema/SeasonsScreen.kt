package com.example.cinema

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier

@Composable
fun SeasonsScreen(viewModel: SeasonsViewModel) {
    val seasons by viewModel.seasons.collectAsState()
    val error by viewModel.error.collectAsState()
    Column(modifier = Modifier.fillMaxSize()) {
        if (error != null) {
            Text("Ошибка: $error")
        }
        LazyColumn {
            items(seasons) { season ->
                Text("Сезон ${season.number}", style = MaterialTheme.typography.titleMedium)
                season.episodes.forEach { e ->
                    Text("${e.episodeNumber}. ${e.nameRu ?: e.nameEn ?: ""}")
                }
            }
        }
    }
}


