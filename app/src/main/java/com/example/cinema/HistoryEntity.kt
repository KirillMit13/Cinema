package com.example.cinema

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "history")
data class HistoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val type: String,
    val refId: Int,
    val timestamp: Long
)

data class HistoryWithFilm(
    val id: Long,
    val type: String,
    val refId: Int,
    val timestamp: Long,
    val filmId: Int?,
    val filmTitle: String?,
    val filmPosterUrl: String?,
    val filmIsWatched: Boolean?
)


