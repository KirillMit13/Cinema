package com.example.cinema

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "films")
data class FilmEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val posterUrl: String?,
    val isWatched: Boolean = false,
    val isFavorite: Boolean = false
)