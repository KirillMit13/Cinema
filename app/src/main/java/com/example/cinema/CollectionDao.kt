package com.example.cinema

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface CollectionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCollection(collection: CollectionEntity): Long

    @Query("SELECT * FROM collections")
    fun getCollections(): Flow<List<CollectionEntity>>

    @Query("SELECT * FROM collections WHERE id = :id")
    fun getCollection(id: Long): Flow<CollectionEntity?>

    @Query("DELETE FROM collections WHERE id = :id AND type = 'CUSTOM'")
    suspend fun deleteCustomCollection(id: Long): Int

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addFilmToCollection(crossRef: FilmCollectionCrossRef): Long

    @Query("DELETE FROM film_collection_cross_ref WHERE filmId = :filmId AND collectionId = :collectionId")
    suspend fun removeFilmFromCollection(filmId: Int, collectionId: Long): Int

    @Query("SELECT COUNT(*) FROM film_collection_cross_ref WHERE collectionId = :collectionId")
    fun getCollectionCount(collectionId: Long): Flow<Int>

    @Transaction
    @Query("SELECT films.* FROM films INNER JOIN film_collection_cross_ref ON films.id = film_collection_cross_ref.filmId WHERE film_collection_cross_ref.collectionId = :collectionId")
    fun getFilmsForCollection(collectionId: Long): Flow<List<FilmEntity>>

    @Query("SELECT * FROM collections WHERE type = :type LIMIT 1")
    suspend fun getCollectionByType(type: String): CollectionEntity?

    @Query("SELECT EXISTS(SELECT 1 FROM film_collection_cross_ref WHERE filmId = :filmId AND collectionId = :collectionId)")
    suspend fun isFilmInCollection(filmId: Int, collectionId: Long): Boolean

    @Query("SELECT EXISTS(SELECT 1 FROM film_collection_cross_ref WHERE filmId = :filmId)")
    fun isFilmInAnyCollectionFlow(filmId: Int): kotlinx.coroutines.flow.Flow<Boolean>
}