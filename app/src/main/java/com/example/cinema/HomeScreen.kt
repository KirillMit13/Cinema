package com.example.cinema

import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.cinema.domain.model.Film
import com.example.cinema.ui.components.*

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onFilmClick: (Int) -> Unit,
    onSectionClick: (String) -> Unit
) {
    val sections by viewModel.sections.collectAsStateWithLifecycle(emptyList())
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle(false)
    val error by viewModel.errorMessage.collectAsStateWithLifecycle(null)

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
                    FilmSection(
                        section = section,
                        onFilmClick = onFilmClick,
                        onSectionClick = onSectionClick
                    )
                }
            }
        }
    }
}

@Composable
private fun FilmSection(
    section: FilmSection,
    onFilmClick: (Int) -> Unit,
    onSectionClick: (String) -> Unit
) {
    Column(
        modifier = Modifier.padding(vertical = 8.dp)
    ) {
        // Section header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = section.sectionTitle,
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.weight(1f)
            )
            
            if (section.showAll) {
                TextButton(
                    onClick = { onSectionClick(section.sectionTitle) }
                ) {
                    Text(
                        text = "Все",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }

        // Films row
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(
                items = section.films,
                key = { film: com.example.cinema.domain.model.Film -> film.id }
            ) { film: com.example.cinema.domain.model.Film ->
                FilmCard(
                    film = film,
                    onClick = { onFilmClick(film.id) }
                )
            }
        }
    }
}

@Composable
private fun FilmCard(
    film: Film,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.width(140.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column {
            // Poster with overlay
            Box {
                AsyncImage(
                    model = film.posterUrl,
                    contentDescription = film.title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentScale = ContentScale.Crop
                )
                
                // Watched indicator
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
                
                // Play button overlay
                Box(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(48.dp)
                        .clip(RoundedCornerShape(24.dp))
                        .background(Color.Black.copy(alpha = 0.5f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Смотреть",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
            
            // Film info
            Column(
                modifier = Modifier.padding(12.dp)
            ) {
                Text(
                    text = film.title,
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Medium
                    ),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                // Rating and year
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (film.rating != null) {
                        Text(
                            text = "★ ${film.rating}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    
                    if (film.year != null) {
                        if (film.rating != null) {
                            Text(
                                text = " • ",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Text(
                            text = film.year.toString(),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SectionShimmer() {
    Column(
        modifier = Modifier.padding(vertical = 8.dp)
    ) {
        // Section header shimmer
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ShimmerEffect()
            Spacer(modifier = Modifier.weight(1f))
            ShimmerEffect()
        }
        
        // Films row shimmer
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(5) {
                FilmCardShimmer()
            }
        }
    }
}