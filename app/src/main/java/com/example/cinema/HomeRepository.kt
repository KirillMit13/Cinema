package com.example.cinema
import com.example.cinema.domain.model.Film
import com.example.cinema.data.mapper.toDomain
import com.example.cinema.data.remote.model.KinopoiskService

class HomeRepository(
    private val api: KinopoiskService,
    private val filmDao: FilmDao
) {
    suspend fun loadHomeSections(): List<FilmSection> {
        println("HomeRepository: Starting to load home sections...")
        
        val premieresSection = try {
            println("HomeRepository: Loading premieres...")
            val now = java.util.Calendar.getInstance()
            val year = now.get(java.util.Calendar.YEAR)
            val month = now.getDisplayName(java.util.Calendar.MONTH, java.util.Calendar.LONG, java.util.Locale.ENGLISH)!!.uppercase()
            val premieres = api.getPremieres(year, month)
            println("HomeRepository: Premieres loaded: ${premieres.items.size} items")
            FilmSection(
                sectionTitle = "Премьеры",
                films = premieres.items.map { it.toDomain() }
            )
        } catch (e: Exception) {
            println("HomeRepository: Error loading premieres: ${e.message}")
            e.printStackTrace()
            FilmSection(sectionTitle = "Премьеры", films = emptyList())
        }

        val topFilmsSection = try {
            println("HomeRepository: Loading top films...")
            val topFilms = api.getTopFilms(type = "TOP_250_MOVIES", page = 1)
            println("HomeRepository: Top films loaded: ${topFilms.items.size} items")
            FilmSection(
                sectionTitle = "Топ-250",
                films = topFilms.items.map { it.toDomain() },
                showAll = true
            )
        } catch (e: Exception) {
            println("HomeRepository: Error loading top films: ${e.message}")
            e.printStackTrace()
            FilmSection(sectionTitle = "Топ-250", films = emptyList(), showAll = true)
        }

        val popularSection = try {
            println("HomeRepository: Loading popular films...")
            val popularFilms = api.getTopFilms(type = "TOP_POPULAR_ALL", page = 1)
            println("HomeRepository: Popular films loaded: ${popularFilms.items.size} items")
            if (popularFilms.items.isNotEmpty()) {
                FilmSection(
                    sectionTitle = "Популярное",
                    films = popularFilms.items.map { it.toDomain() },
                    showAll = true
                )
            } else {
                println("HomeRepository: Popular films API returned empty list")
                FilmSection(sectionTitle = "Популярное", films = emptyList(), showAll = true)
            }
        } catch (e: Exception) {
            println("HomeRepository: Error loading popular films: ${e.message}")
            e.printStackTrace()
            FilmSection(sectionTitle = "Популярное", films = emptyList(), showAll = true)
        }

        val watchedIds = filmDao.getWatchedIds().toSet()
        return listOf(premieresSection, popularSection, topFilmsSection).map { section ->
            FilmSection(
                sectionTitle = section.sectionTitle,
                films = section.films.map { film ->
                    Film(
                        id = film.id,
                        title = film.title,
                        posterUrl = film.posterUrl,
                        rating = film.rating,
                        year = film.year,
                        genres = film.genres,
                        isWatched = watchedIds.contains(film.id))
                },
                showAll = section.showAll
            )
        }
    }
}