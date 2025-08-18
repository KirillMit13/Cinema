package com.example.cinema.data.mapper

import com.example.cinema.data.remote.model.FilteredFilm
import com.example.cinema.data.remote.model.PremiereFilm
import com.example.cinema.data.remote.model.TopFilm
import com.example.cinema.domain.model.Film
import com.example.cinema.domain.model.FilmDetails
import com.example.cinema.data.remote.model.FilmDetailsResponse
import com.example.cinema.data.remote.model.SearchFilm
import com.example.cinema.data.remote.model.SimilarFilm
import com.example.cinema.data.remote.model.PersonResponse
import com.example.cinema.data.remote.model.StaffItem
import com.example.cinema.domain.model.Person

fun TopFilm.toDomain(): Film = Film(
    id = id,
    title = title ?: "Без названия",
    posterUrl = posterUrl,
    rating = rating?.toString(),
    year = year,
    genres = genres?.mapNotNull { it.genre } ?: emptyList()
)

fun FilteredFilm.toDomain(): Film = Film(
    id = id,
    title = title ?: "Без названия",
    posterUrl = posterUrl,
    rating = rating?.toString(),
    year = year,
    genres = genres?.mapNotNull { it.genre } ?: emptyList()
)

fun PremiereFilm.toDomain(): Film = Film(
    id = id,
    title = title ?: "Без названия",
    posterUrl = posterUrl,
    rating = null,
    year = year,
    genres = emptyList()
)

fun FilmDetailsResponse.toDomain(): FilmDetails = FilmDetails(
    id = id,
    title = title ?: "Без названия",
    originalTitle = nameOriginal,
    posterUrl = posterUrl,
    rating = rating?.toString(),
    year = year,
    shortDescription = shortDescription,
    description = description,
    type = type,
    genres = genres?.mapNotNull { it.genre } ?: emptyList(),
    imdbId = imdbId
)

fun SearchFilm.toDomain(): Film = Film(
    id = id,
    title = title ?: "Без названия",
    posterUrl = posterUrl,
    rating = rating,
    year = year?.toIntOrNull(),
    genres = emptyList()
)

fun SimilarFilm.toDomain(): Film = Film(
    id = id,
    title = title ?: "Без названия",
    posterUrl = posterUrl,
    rating = null,
    year = null,
    genres = emptyList()
)

fun PersonResponse.toBestFilms(): List<Film> = (films ?: emptyList())
    .sortedByDescending { it.rating?.toDoubleOrNull() ?: 0.0 }
    .take(10)
    .map { Film(id = it.id, title = it.title ?: "Без названия", posterUrl = null, rating = it.rating, year = null, genres = emptyList()) }

fun StaffItem.toDomainPerson(): Person = Person(
    id = staffId,
    name = nameRu ?: nameEn ?: "Без имени",
    photoUrl = posterUrl,
    role = description,
    profession = professionText
)


