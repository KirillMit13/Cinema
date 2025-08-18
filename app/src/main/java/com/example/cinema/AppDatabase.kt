package com.example.cinema

import androidx.room.Database
import androidx.room.RoomDatabase

const val DATABASE_VERSION = 1

@Database(
    entities = [FilmEntity::class, CollectionEntity::class, FilmCollectionCrossRef::class, HistoryEntity::class],
    version = DATABASE_VERSION,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun filmDao(): FilmDao
    abstract fun collectionDao(): CollectionDao
    abstract fun historyDao(): HistoryDao
}