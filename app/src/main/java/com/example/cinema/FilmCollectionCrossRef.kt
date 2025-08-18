package com.example.cinema

import androidx.room.Entity

@Entity(tableName = "film_collection_cross_ref", primaryKeys = ["filmId", "collectionId"])
data class FilmCollectionCrossRef(
    val filmId: Int,
    val collectionId: Long
)


