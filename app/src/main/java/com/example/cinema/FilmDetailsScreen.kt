package com.example.cinema

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.cinema.ui.components.*
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.shape.CircleShape

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilmDetailsScreen(
    filmId: Int,
    viewModel: FilmDetailsViewModel,
    onBackClick: () -> Unit = {},
    onFilmClick: (Int) -> Unit = {},
    onPersonClick: (Int) -> Unit = {},
    appContainer: AppContainer? = null
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val error by viewModel.error.collectAsStateWithLifecycle()
    val staff by viewModel.staff.collectAsStateWithLifecycle()
    val similars by viewModel.similars.collectAsStateWithLifecycle()
    val inAnyCollection by viewModel.isInAnyCollection.collectAsStateWithLifecycle()

    LaunchedEffect(filmId) {
        viewModel.load()
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // Top bar
        TopAppBar(
            title = { Text("Детали фильма") },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
            }
        )

        when {
            error != null -> {
                ErrorMessage(
                    message = "Ошибка загрузки фильма: ${error}",
                    onRetry = { viewModel.load() },
                    modifier = Modifier.padding(16.dp)
                )
            }
            state == null -> {
                FilmDetailsShimmer()
            }
            else -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 16.dp)
                ) {
                    item {
                        FilmHeader(
                            film = state!!,
                            onFavorite = { viewModel.toggleFavorite() },
                            onWatchlist = { viewModel.toggleWatchlist() },
                            onWatched = { viewModel.toggleWatched() },
                            inAnyCollection = inAnyCollection
                        )
                    }

                    item {
                        var showPicker by remember { mutableStateOf(false) }
                        if (showPicker && appContainer != null && state != null) {
                            CollectionsPickerSheet(appContainer = appContainer, film = com.example.cinema.domain.model.Film(
                                id = state!!.id,
                                title = state!!.title,
                                posterUrl = state!!.posterUrl,
                                rating = state!!.rating,
                                year = state!!.year,
                                genres = state!!.genres ?: emptyList(),
                                isWatched = false
                            )) { showPicker = false }
                        }
                        FilmActions(
                            onGallery = { /* TODO */ },
                            onSeasons = { /* TODO */ },
                            onCollections = { showPicker = true },
                            onShare = { /* TODO */ }
                        )
                    }

                    if (staff.isNotEmpty()) {
                        item {
                            StaffSection(
                                staff = staff,
                                onPersonClick = onPersonClick
                            )
                        }
                    }

                    if (similars.isNotEmpty()) {
                        item {
                            SimilarFilmsSection(
                                films = similars,
                                onFilmClick = onFilmClick
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun FilmHeader(
    film: com.example.cinema.domain.model.FilmDetails,
    onFavorite: () -> Unit,
    onWatchlist: () -> Unit,
    onWatched: () -> Unit,
    inAnyCollection: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            FilmPosterWithRating(
                imageUrl = film.posterUrl,
                rating = film.rating?.toDoubleOrNull(),
                modifier = Modifier
                    .size(140.dp, 200.dp)
                    .clip(RoundedCornerShape(12.dp))
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = film.title,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                film.genres?.let { genres ->
                    Text(
                        text = genres.joinToString(", "),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Action buttons
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    IconButton(onClick = onFavorite) {
                        Icon(
                            Icons.Default.Favorite,
                            contentDescription = "Favorite",
                            tint = if (film.isFavorite) Color(0xFFFF4081) else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    IconButton(onClick = onWatchlist) {
                        Icon(
                            Icons.Default.Bookmark,
                            contentDescription = "Watchlist",
                            tint = if (film.isInWatchlist || inAnyCollection) Color(0xFF42A5F5) else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    IconButton(onClick = onWatched) {
                        Icon(
                            Icons.Default.Check,
                            contentDescription = "Watched",
                            tint = if (film.isWatched) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        film.description?.let { description ->
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
private fun FilmActions(
    onGallery: () -> Unit,
    onSeasons: () -> Unit,
    onCollections: () -> Unit,
    onShare: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        ActionButton(
            icon = Icons.Default.PhotoLibrary,
            text = "Галерея",
            onClick = onGallery
        )
        ActionButton(
            icon = Icons.Default.List,
            text = "Сезоны",
            onClick = onSeasons
        )
        ActionButton(
            icon = Icons.Default.Folder,
            text = "Коллекции",
            onClick = onCollections
        )
        ActionButton(
            icon = Icons.Default.Share,
            text = "Поделиться",
            onClick = onShare
        )
    }
}

@Composable
private fun ActionButton(
    icon: ImageVector,
    text: String,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {
        Icon(
            imageVector = icon,
            contentDescription = text,
            modifier = Modifier.size(24.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun StaffSection(
    staff: List<com.example.cinema.domain.model.Person>,
    onPersonClick: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "Актеры и съемочная группа",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(12.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(staff) { person ->
                PersonPhoto(
                    imageUrl = person.photoUrl,
                    modifier = Modifier
                        .size(80.dp, 80.dp)
                        .clip(CircleShape)
                        .clickable { onPersonClick(person.id) }
                )
            }
        }
    }
}

@Composable
private fun SimilarFilmsSection(
    films: List<com.example.cinema.domain.model.Film>,
    onFilmClick: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "Похожие фильмы",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(12.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(films) { film ->
                FilmPosterWithRating(
                    imageUrl = film.posterUrl,
                    rating = film.rating?.toDoubleOrNull(),
                    modifier = Modifier
                        .size(120.dp, 180.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { onFilmClick(film.id) }
                )
            }
        }
    }
}


