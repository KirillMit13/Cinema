package com.example.cinema

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import com.example.cinema.domain.model.Film
import com.example.cinema.domain.model.Collection as DomainCollection
import com.example.cinema.domain.model.Collection
import com.example.cinema.CollectionEntity
import com.example.cinema.FilmEntity
import com.example.cinema.FilmCollectionCrossRef

class CollectionsRepository(
    private val collectionDao: CollectionDao,
    private val filmDao: FilmDao
) {
    companion object {
        const val TYPE_FAVORITES = "FAVORITES"
        const val TYPE_WATCHLIST = "WATCHLIST"
        const val TYPE_CUSTOM = "CUSTOM"
    }

    fun getCollections(): Flow<List<DomainCollection>> = collectionDao.getCollections().map { entities ->
        entities.map { entity ->
            DomainCollection(
                id = entity.id,
                name = entity.name,
                description = null,
                filmCount = 0 // Временно устанавливаем 0, обновим ниже
            )
        }
    }

    fun getCollectionsWithCounts(): Flow<List<DomainCollection>> = collectionDao.getCollections().map { entities ->
        kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.IO) {
            entities.map { entity ->
                val filmCount = collectionDao.getCollectionCountSync(entity.id)
                DomainCollection(
                    id = entity.id,
                    name = entity.name,
                    description = null,
                    filmCount = filmCount
                )
            }
        }
    }

    fun getCollection(collectionId: Long): Flow<DomainCollection?> = collectionDao.getCollection(collectionId).map { entity ->
        entity?.let {
            DomainCollection(
                id = it.id,
                name = it.name,
                description = null,
                filmCount = 0
            )
        }
    }

    fun getCollectionCount(collectionId: Long): Flow<Int> = collectionDao.getCollectionCount(collectionId)

    fun isInAnyCollectionFlow(filmId: Int): Flow<Boolean> = collectionDao.isFilmInAnyCollectionFlow(filmId)

    fun getFilmsForCollection(collectionId: Long): Flow<List<Film>> =
        collectionDao.getFilmsForCollection(collectionId).map { entities ->
            entities.map { e ->
                Film(
                    id = e.id,
                    title = e.title,
                    posterUrl = e.posterUrl,
                    rating = null,
                    year = null,
                    genres = emptyList(),
                    isWatched = e.isWatched
                )
            }
        }

    @Suppress("unused")
    suspend fun ensureSystemCollections(): Pair<Long, Long> {
        val favoritesId = collectionDao.insertCollection(
            CollectionEntity(name = "Любимые", type = TYPE_FAVORITES)
        )
        val watchlistId = collectionDao.insertCollection(
            CollectionEntity(name = "Хочу посмотреть", type = TYPE_WATCHLIST)
        )
        return favoritesId to watchlistId
    }

    suspend fun createCustomCollection(name: String): Long =
        collectionDao.insertCollection(CollectionEntity(name = name, type = TYPE_CUSTOM))

    suspend fun deleteCustomCollection(id: Long) = collectionDao.deleteCustomCollection(id)

    suspend fun addFilmToCollection(film: Film, collectionId: Long) {
        filmDao.insertFilm(
            FilmEntity(
                id = film.id,
                title = film.title,
                posterUrl = film.posterUrl,
                isWatched = film.isWatched,
                isFavorite = false
            )
        )
        collectionDao.addFilmToCollection(FilmCollectionCrossRef(filmId = film.id, collectionId = collectionId))
    }

    suspend fun removeFilmFromCollection(filmId: Int, collectionId: Long) =
        collectionDao.removeFilmFromCollection(filmId, collectionId)

    suspend fun toggleFavorite(film: Film) {
        val favorites = collectionDao.getCollectionByType(TYPE_FAVORITES)
            ?: CollectionEntity(name = "Любимые", type = TYPE_FAVORITES).let { CollectionEntity(id = collectionDao.insertCollection(it), name = it.name, type = it.type) }
        val inCollection = collectionDao.isFilmInCollection(film.id, favorites.id)
        if (inCollection) {
            collectionDao.removeFilmFromCollection(film.id, favorites.id)
        } else {
            addFilmToCollection(film, favorites.id)
        }
    }

    suspend fun toggleWatchlist(film: Film) {
        val watchlist = collectionDao.getCollectionByType(TYPE_WATCHLIST)
            ?: CollectionEntity(name = "Хочу посмотреть", type = TYPE_WATCHLIST).let { CollectionEntity(id = collectionDao.insertCollection(it), name = it.name, type = it.type) }
        val inCollection = collectionDao.isFilmInCollection(film.id, watchlist.id)
        if (inCollection) {
            collectionDao.removeFilmFromCollection(film.id, watchlist.id)
        } else {
            addFilmToCollection(film, watchlist.id)
        }
    }

    suspend fun toggleWatched(film: Film) {
        filmDao.setWatched(film.id, !filmDao.isWatched(film.id))
    }

    suspend fun isInCollection(filmId: Int, collectionId: Long): Boolean =
        collectionDao.isFilmInCollection(filmId, collectionId)

    suspend fun toggleInCollection(film: Film, collectionId: Long): Boolean {
        val inCollection = isInCollection(film.id, collectionId)
        if (inCollection) {
            removeFilmFromCollection(film.id, collectionId)
        } else {
            addFilmToCollection(film, collectionId)
        }
        return !inCollection
    }
}


