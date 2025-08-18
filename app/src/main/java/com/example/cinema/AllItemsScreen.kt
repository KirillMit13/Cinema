package com.example.cinema

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cinema.domain.model.Film
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import coil.compose.AsyncImage
import com.example.cinema.ui.components.*

@Composable
fun AllItemsScreen(
    films: List<Film>,
    onFilmClick: (Int) -> Unit = {}
) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(
            items = films,
            key = { film: Film -> film.id }
        ) { film: Film ->
            FilmListItem(
                film = film,
                onClick = { onFilmClick(film.id) }
            )
        }
    }
}

@Composable
fun AllItemsPagingScreen(
    flow: Flow<PagingData<Film>>,
    onFilmClick: (Int) -> Unit = {}
) {
    val lazyItems = flow.collectAsLazyPagingItems()

    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Refresh loading state
        if (lazyItems.loadState.refresh is androidx.paging.LoadState.Loading) {
            item {
                LoadingState(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 24.dp)
                )
            }
        }

        // Refresh error state
        if (lazyItems.loadState.refresh is androidx.paging.LoadState.Error) {
            item {
                val error = (lazyItems.loadState.refresh as androidx.paging.LoadState.Error).error
                ErrorMessage(
                    message = "Ошибка загрузки: ${error.message}",
                    onRetry = { lazyItems.retry() },
                    modifier = Modifier.padding(16.dp)
                )
            }
        }

        // Items
        items(lazyItems.itemCount) { index ->
            val film = lazyItems[index]
            if (film != null) {
                FilmListItem(
                    film = film,
                    onClick = { onFilmClick(film.id) }
                )
            }
        }
        
        // Loading state
        if (lazyItems.loadState.append is androidx.paging.LoadState.Loading) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }
        
        // Error state
        if (lazyItems.loadState.append is androidx.paging.LoadState.Error) {
            item {
                val error = (lazyItems.loadState.append as androidx.paging.LoadState.Error).error
                ErrorMessage(
                    message = "Ошибка загрузки: ${error.message}",
                    onRetry = { lazyItems.retry() },
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
        
        // Empty state
        if (lazyItems.loadState.refresh is androidx.paging.LoadState.NotLoading &&
            lazyItems.itemCount == 0) {
            item {
                EmptyState(
                    message = "Нет доступных фильмов",
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}

@Composable
private fun FilmListItem(
    film: Film,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Poster
            AsyncImage(
                model = film.posterUrl,
                contentDescription = film.title,
                modifier = Modifier
                    .size(80.dp, 120.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
            
            Spacer(modifier = Modifier.width(12.dp))
            
            // Film info
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = film.title,
                    style = MaterialTheme.typography.titleMedium.copy(
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
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    
                    if (film.year != null) {
                        if (film.rating != null) {
                            Text(
                                text = " • ",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Text(
                            text = film.year.toString(),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(4.dp))
                
                // Genres
                if (film.genres.isNotEmpty()) {
                    Text(
                        text = film.genres.joinToString(", "),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                
                // Watched indicator
                if (film.isWatched) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(top = 4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Просмотрено",
                            modifier = Modifier.size(16.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Просмотрено",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
            
            // Action button
            IconButton(onClick = onClick) {
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = "Открыть",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}


