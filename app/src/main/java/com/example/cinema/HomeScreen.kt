package com.example.cinema

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.cinema.domain.model.Film
import com.example.cinema.ui.components.*
import com.example.cinema.ui.components.NeonColors
import com.example.cinema.ui.components.NeonCard
import com.example.cinema.ui.components.NeonTitle
import com.example.cinema.ui.components.NeonSurface
import com.example.cinema.ui.components.SectionShimmer
import com.example.cinema.ui.components.ErrorMessage
import com.example.cinema.ui.components.EmptyState

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onFilmClick: (Int) -> Unit,
    onSectionClick: (String) -> Unit
) {
    val sections by viewModel.sections.collectAsStateWithLifecycle(emptyList())
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle(false)
    val error by viewModel.errorMessage.collectAsStateWithLifecycle(null)

    NeonSurface(
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            when {
                isLoading -> {
                    items(3) {
                        SectionShimmer()
                    }
                }

                error != null -> {
                    item {
                        ErrorMessage(
                            message = "Ошибка загрузки: ${error}",
                            onRetry = { viewModel.loadData() },
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }

                sections.isEmpty() -> {
                    item {
                        EmptyState(
                            message = "Нет доступных фильмов. Проверьте подключение к интернету или попробуйте позже.",
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }

                else -> {
                    items(
                        items = sections,
                        key = { section: FilmSection -> section.sectionTitle }
                    ) { section: FilmSection ->
                        FilmSectionContent(
                            section = section,
                            onFilmClick = onFilmClick,
                            onSectionClick = onSectionClick
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun FilmSectionContent(
    section: FilmSection,
    onFilmClick: (Int) -> Unit,
    onSectionClick: (String) -> Unit
) {
    Column(
        modifier = Modifier.padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            NeonTitle(
                text = section.sectionTitle
            )
        }

        val previewFilms = remember(section.films) { section.films.take(10) }
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            itemsIndexed(
                items = previewFilms,
                key = { index, film: Film -> "${film.id}_${index}" }
            ) { _, film: Film ->
                FilmCard(
                    film = film,
                    onClick = { onFilmClick(film.id) }
                )
            }

            item(key = "more_${section.sectionTitle}") {
                Box(
                    modifier = Modifier
                        .width(140.dp)
                        .height(280.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .clickable { onSectionClick(section.sectionTitle) }
                        .background(neonGradient),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = "Все",
                        tint = Color.White,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun FilmCard(
    film: Film,
    onClick: () -> Unit
) {
    NeonCard(
        modifier = Modifier
            .width(140.dp)
            .height(280.dp)
    ) {
        Box(
            modifier = Modifier.clickable { onClick() }
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                ) {
                    AsyncImage(
                        model = film.posterUrl,
                        contentDescription = film.title,
                        modifier = Modifier
                            .fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )

                    if (film.isWatched) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(8.dp)
                                .size(24.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color.Black.copy(alpha = 0.7f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Просмотрено",
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .size(48.dp)
                            .clip(RoundedCornerShape(24.dp))
                            .background(Color.Black.copy(alpha = 0.7f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = "Воспроизвести",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .weight(1f),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = film.title,
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.Medium
                        ),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.fillMaxWidth(),
                        color = NeonColors.TextPrimary
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        film.year?.let { year ->
                            Text(
                                text = year.toString(),
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        
                        film.rating?.let { rating ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = "Рейтинг",
                                    tint = Color(0xFFFFD700),
                                    modifier = Modifier.size(12.dp)
                                )
                                Spacer(modifier = Modifier.width(2.dp))
                                Text(
                                    text = rating?.toDoubleOrNull()?.let { String.format("%.1f", it) } ?: rating,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}