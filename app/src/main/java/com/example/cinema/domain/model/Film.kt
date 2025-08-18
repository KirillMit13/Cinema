package com.example.cinema.domain.model

data class Film(
    val id: Int,
    val title: String,
    val posterUrl: String?,
    val rating: String?,
    val year: Int?,
    val genres: List<String>,
    val isWatched: Boolean = false
)


