package com.example.cinema

import com.example.cinema.domain.model.Film

data class FilmSection(
    val sectionTitle: String,
    val films: List<Film>,
    val showAll: Boolean = false
)