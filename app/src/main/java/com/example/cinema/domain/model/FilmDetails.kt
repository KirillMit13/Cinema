package com.example.cinema.domain.model

data class FilmDetails(
    val id: Int,
    val title: String,
    val originalTitle: String?,
    val posterUrl: String?,
    val rating: String?,
    val year: Int?,
    val shortDescription: String?,
    val description: String?,
    val type: String?,
    val genres: List<String>,
    val imdbId: String?,
    val isFavorite: Boolean = false,
    val isInWatchlist: Boolean = false,
    val isWatched: Boolean = false
)


