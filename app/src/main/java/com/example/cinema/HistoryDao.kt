package com.example.cinema

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface HistoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: HistoryEntity)

    @Query("SELECT * FROM history ORDER BY timestamp DESC LIMIT :limit")
    fun getRecent(limit: Int): Flow<List<HistoryEntity>>

    @Transaction
    @Query("""
        SELECT h.*, f.id as filmId, f.title AS filmTitle, f.posterUrl AS filmPosterUrl, f.isWatched AS filmIsWatched
        FROM history h
        LEFT JOIN films f ON h.refId = f.id AND h.type = 'FILM'
        ORDER BY h.timestamp DESC
        LIMIT :limit
    """)
    fun getRecentWithFilmData(limit: Int): Flow<List<HistoryWithFilm>>

    @Query("DELETE FROM history")
    suspend fun clearAll(): Int
}


