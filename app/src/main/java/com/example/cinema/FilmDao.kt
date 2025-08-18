package com.example.cinema

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FilmDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFilm(film: FilmEntity): Long

    @Query("SELECT * FROM films WHERE isFavorite = 1")
    fun getFavorites(): Flow<List<FilmEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM films WHERE id = :filmId AND isWatched = 1)")
    suspend fun isWatched(filmId: Int): Boolean

    @Query("UPDATE films SET isWatched = :watched WHERE id = :filmId")
    suspend fun setWatched(filmId: Int, watched: Boolean): Int

    @Query("UPDATE films SET isFavorite = :favorite WHERE id = :filmId")
    suspend fun setFavorite(filmId: Int, favorite: Boolean): Int

    @Query("SELECT id FROM films WHERE isWatched = 1")
    suspend fun getWatchedIds(): List<Int>

    @Suppress("unused")
    @Query("SELECT id FROM films WHERE isFavorite = 1")
    suspend fun getFavoriteIds(): List<Int>

    @Query("SELECT * FROM films WHERE isWatched = 1")
    fun getWatchedFilms(): Flow<List<FilmEntity>>
}