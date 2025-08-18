package com.example.cinema.data.remote.model

import com.google.gson.annotations.SerializedName

data class FilmDetailsResponse(
    @SerializedName("kinopoiskId") val id: Int,
    @SerializedName("nameRu") val title: String?,
    @SerializedName("nameOriginal") val nameOriginal: String?,
    @SerializedName("posterUrl") val posterUrl: String?,
    @SerializedName("ratingKinopoisk") val rating: Double?,
    @SerializedName("year") val year: Int?,
    @SerializedName("shortDescription") val shortDescription: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("type") val type: String?,
    @SerializedName("genres") val genres: List<Genre>?,
    @SerializedName("imdbId") val imdbId: String?
)


