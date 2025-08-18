package com.example.cinema

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "history")
data class HistoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val type: String, // FILM, PERSON
    val refId: Int,
    val timestamp: Long
)


