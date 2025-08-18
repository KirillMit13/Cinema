package com.example.cinema

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun GalleryScreen(viewModel: GalleryViewModel, onOpenFull: (Int) -> Unit) {
    val types by viewModel.types.collectAsState()
    val images by viewModel.images.collectAsState()

    Column(modifier = Modifier.padding(16.dp)) {
        if (types.isNotEmpty()) {
            LazyRow {
                items(types) { t ->
                    AssistChip(onClick = { viewModel.load(t) }, label = { Text(t) }, modifier = Modifier.padding(end = 8.dp))
                }
            }
        }
        LazyRow {
            items(images.indices.toList()) { index ->
                val url = images[index]
                Column(modifier = Modifier.padding(8.dp).clickable { onOpenFull(index) }) {
                    AsyncImage(model = url, contentDescription = "image")
                }
            }
        }
    }
}


